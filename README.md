# INTOUCH
Dijital dünyada ben de varım diyen, müşterilerinin fikir, öneri ve şikayetlerini önemseyen her bir kuruluş için müşteri sadakati ve verimlilik üzerinde olumlu etkiler yaratacak metin sınıflandırma çözümleri sunuyoruz.

>  *<< İnsanlar, bot’lara da hikayelerini anlatmak ister. Bot’un
anlamakta zorlandığı kompleks, uzun ve birden fazla
amaç içeren mesajlarını anlamak insanlarla bot’ları
birbirlerine bir adım daha yaklaştırır. >>*


| Problem | Çözüm | Uygulama |
|     :---:      |     :---:      |     :---:      |
| Günümüzde geliştirilen NLU algoritmaları kullanıcıların mesajlarından tek bir amaç çıkarmak üzerine çalışmaktadır. Ancak özellikle Türkiye’deki kullanıcıların kompleks, birden fazla amaç içeren mesajları bu şekilde çözümlenemiyor.   | Bu tarz mesajlarda metinlerin parçalanarak birden fazla amaç tespiti yapıldıktan sonra, bu amaçların çoklu kombinasyonuna uygun mantıksal çıkarımlar yapılması sağlanabilir.     | Uzun içeriklerin özetini çıkarılır. Özeti çıkarılan cümlelerin intent’le eşleştirilir. Birden fazla intent çıkarılması durumunda, varyasyonlara göre uygun çıktının oluşması sağlanır.    |


### A.I.JOE TAKIM ÜYELERİ
* **Ayça Topal**, [@aycatopal](https://github.com/aycatopal)
* **Osman Burak Kazkılınç**, [@burakkazkilinc](https://github.com/burakkazkilinc)
* **Oğuzhan Karaduman**, [@okaraduman](https://github.com/okaraduman)

> Projeyi geliştirmeye başlamadan önce oluşturduğumuz bileşenleri, uyguladığımız metodolojiyi, görev dağımlarını, teknik mimariyi, uygulama arayüzünün mock çizimlerini görmek için [buraya tıklayınız.](https://trello.com/b/uHX7tQjW/intouch)


### KURULUM
Uygulamayı kullanmanız için indirdikten sonra aşağıdaki talimatları izlemeniz gerekmektedir:

1. Hazırlamış olduğumuz corpus'umuzu indirmeniz gerekmektedir. İndirmek için [tıklayınız.](https://drive.google.com/drive/folders/1X8ED-3wyIGODAhFokCxZ17exP1lyrhGy?usp=sharing)
2. Corpus'u train etmek için Zemberek Jar'ını kullanıyoruz. İndirmek için [tıklayınız.](https://drive.google.com/drive/folders/1X8ED-3wyIGODAhFokCxZ17exP1lyrhGy?usp=sharing)
3. Zemberek Jar'ının bulunduğu dizinde aşağıdaki komutu çalıştırarak corpus'u train edip model elde ediyoruz.
```java
java -jar zemberek-full.jar TrainClassifier -i corpus -o corpus.model -lr 0.1 -ec 50
```
4. Oluşturduğumuz modeli /resources/datasets/ dizinine kopyalayarak projemizi çalışır hale getiriyoruz.

### KULLANIM
API olarak kullanabilir ya da dilerseniz bu proje için oluşturduğumuz açık kaynak Android uygulamamızı [indirerek](https://github.com/okaraduman/intouch-android) bilgisayarınızda veya telefonunuzda deneyimleyebilirsiniz. 

Örnek istek ve yanıtları aşağıda bulabilirsiniz: 

| HTTP METHOD | URL | BODY |
|     :---:      |     :---:      |     :---:      |
| GET   | /intouch/sikayetvar/{companyName}  --> (i.e /intouch/sikayetvar/garanti bbva)   | N/A    |
| GET   | /intouch/twitter/{companyName}  --> (i.e /intouch/twitter/garanti bbva)   | N/A    |

```json
{
    "categoryList": {
      "ATM Sorunları": [
            {
                "originalMessage": "GarantiBBVA On tane bankamatik dolandım atmlerinizde para yok bu ne rezillik sabah kurban parası verecez neden önlemlerinizi almıyorsunuz.",
                "originalMessageUrl": "https://twitter.com/i/web/status/1288932163832950784",
                "summaryText": "garantibbva on tane bankamatik dolanıldım Atm'lerinizde para yok bu ne rezillik sabah kurban parası vereceğiz neden önlemlerinizi almıyorsunuz .",
                "intents": [
                    "ATM Sorunları"
                ],
                "outputMessage": "Değerli müşterimiz,\nBildiriminiz için teşekkür ederiz.\n\nATM'lerimiz ile ilgili talebinizi incelemeye aldık. Konuyu inceleyerek size dönüş yapabilmemiz için iletişim bilgilerinizi de iletmenizi rica ederiz.\nİyi günler dileriz.  \n\nSaygılarımızla,\nGARANTİ BBVA"
            }
      ]
    },
    "totalMessageCount": 1
}
```

### REFERANS
* [Zemberek-NLP](https://github.com/ahmetaa/zemberek-nlp)
* [Teaddict/Turkce Metin Ozetleme](https://github.com/teaddict/turkce-metin-ozetleme-scala)

#Acıkhack2
