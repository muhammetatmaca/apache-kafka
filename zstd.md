
### Zstd Sıkıştırma Algoritması

Bu proje, Kafka mesajlarını iletirken performansı artırmak ve kaynak kullanımını optimize etmek amacıyla **Zstd (Zstandard)** sıkıştırma algoritmasından faydalanır.

#### Zstd Nedir?

Zstd, Facebook tarafından geliştirilmiş, modern ve yüksek performanslı bir kayıpsız veri sıkıştırma algoritmasıdır. Temel amacı, yüksek sıkıştırma oranı sunarken aynı zamanda çok hızlı sıkıştırma ve açma (decompression) işlemleri gerçekleştirmektir.

#### Neden Zstd Kullanılmalı?

Kafka gibi veri akış platformlarında Zstd kullanmanın temel avantajları şunlardır:

1.  **Dengeli Performans:** Geleneksel algoritmalara göre en iyi dengeyi sunar.

    * **Gzip**'e göre çok daha hızlıdır ve benzer veya daha iyi sıkıştırma oranı sağlar.
    * **Snappy** ve **LZ4**'e göre genellikle daha iyi sıkıştırma oranı sunarken, onlara yakın hızlarda çalışır.

2.  **Ağ Trafiğini Azaltma:** Daha yüksek sıkıştırma oranı, mesajların ağ üzerinde daha az yer kaplaması anlamına gelir. Bu, özellikle yüksek hacimli veri akışlarında ağ bant genişliğini verimli kullanmayı sağlar ve gecikmeyi azaltır.

3.  **Düşük CPU Kullanımı:** Hızlı sıkıştırma/açma işlemleri, hem producer (mesajı gönderen) hem de consumer (mesajı alan) tarafında CPU yükünü minimize eder.

4.  **Depolama Maliyetini Düşürme:** Mesajlar broker'larda daha az yer kapladığı için disk kullanımından tasarruf edilir.

#### Kafka için Yapılandırma

Kafka Producer'da Zstd sıkıştırmasını aktif etmek için `compression.type` özelliğini aşağıdaki gibi ayarlamanız yeterlidir:

```properties
# Mesajları Zstd ile sıkıştırmak için kullanılır
compression.type=zstd
```

Bu basit yapılandırma, sistemin genel verimini (throughput) artırırken altyapı maliyetlerini düşürmek için etkili bir yöntemdir.