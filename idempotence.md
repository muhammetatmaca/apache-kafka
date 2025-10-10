Idempotent Producer Yapılandırması
Bu proje, Kafka'ya gönderilen mesajların tekrarını önlemek ve "exactly-once" (tam olarak bir kez) teslimat semantiğini garanti altına almak için Idempotent Producer mekanizmasını kullanır.

Neden Gerekli?
Dağıtık sistemlerde, bir producer'ın gönderdiği mesajın broker'a ulaşıp ulaşmadığına dair onayı (acknowledgement) alamadığı durumlar olabilir (örneğin, geçici ağ sorunları). Bu durumda producer, mesajın kaybolduğunu varsayarak aynı mesajı tekrar gönderebilir. Idempotence olmadan bu senaryo, topic'e aynı mesajın birden çok kopyasının yazılmasına, yani veri tekrarına (data duplication) yol açar.

Nasıl Çalışır?
enable.idempotence=true ayarı yapıldığında Kafka bu sorunu şu şekilde çözer:

Producer ID (PID) ve Sıra Numarası (Sequence Number): Producer başlatıldığında benzersiz bir PID alır. Gönderdiği her mesaja, artan bir sıra numarası ekler.

Broker Tarafında Kontrol: Broker, her (PID, Partition) ikilisi için en son aldığı mesajın sıra numarasını hafızasında tutar.

Tekrar Engelleme: Eğer broker, beklediği sıra numarasından daha düşük veya aynı numaraya sahip bir mesaj alırsa, bunun bir tekrar olduğunu anlar ve mesajı reddeder.

Bu sayede, bir mesaj birden çok kez gönderilse bile topic'e yalnızca bir kez yazılması garanti edilir.

Yapılandırma
Producer ayarlarında idempotence özelliğini aktif etmek için tek yapmanız gereken aşağıdaki konfigürasyonu eklemektir:

Properties

# Idempotence özelliğini aktif eder
enable.idempotence=true

# Güvenilirlik için acks ayarının 'all' olması önerilir
acks=all
Bu yapılandırma, özellikle finansal işlemler, sipariş yönetimi ve stok takibi gibi veri bütünlüğünün kritik olduğu sistemlerde hayati öneme sahiptir.