# INTOUCH
Dijital dünyada ben de varım diyen, müşterilerinin fikir, öneri ve şikayetlerini önemseyen her bir kuruluş için; müşteri sadakati ve verimlilik üzerinde olumlu etkiler metin sınıflandırma çözümleri sunuyoruz.

>  *<< İnsanlar, bot’lara da hikayelerini anlatmak ister. Bot’un
anlamakta zorlandığı kompleks, uzun ve birden fazla
amaç içeren mesajlarını anlamak insanlarla bot’ları
birbirlerine bir adım daha yaklaştırır. >>*


| Problem | Çözüm | Uygulama |
|     :---:      |     :---:      |     :---:      |
| Günümüzde geliştirilen NLU algoritmaları kullanıcıların mesajlarından tek bir amaç çıkarmak üzerine çalışmaktadır. Ancak özellikle Türkiye’deki kullanıcıların kompleks, birden fazla amaç içeren mesajları bu şekilde çözümlenemiyor.   | Bu tarz mesajlarda metinlerin parçalanarak birden fazla amaç tespiti yapıldıktan sonra, bu amaçların çoklu kombinasyonuna uygun mantıksal çıkarımlar yapılması sağlanabilir.     | Uzun içeriklerin özetini çıkarılır. Özeti çıkarılan cümlelerin intent’le eşleştirilir. Birden fazla intent çıkarılması durumunda, varyasyonlara göre uygun çıktının oluşması sağlanır.    |


### TAKIM ÜYELERİ
* **Ayça Topal**, [@aycatopal](https://github.com/aycatopal)
* **Osman Burak Kazkılınç**, [@burakkazkilinc](https://github.com/burakkazkilinc)
* **Oğuzhan Karaduman**, [@okaraduman](https://github.com/okaraduman)


### KURULUM
Uygulamayı kullanmanız için indirdikten sonra aşağıdaki talimatları izlemeniz gerekmektedir:

1. Hazırlamış olduğumuz corpus'umuzu indirmeniz gerekmektedir. İndirmek için [tıklayınız.](https://drive.google.com/drive/folders/1X8ED-3wyIGODAhFokCxZ17exP1lyrhGy?usp=sharing)
2. Corpus'u train etmek için Zemberek Jar'ını kullanıyoruz. İndirmek için [tıklayınız.](https://drive.google.com/drive/folders/1X8ED-3wyIGODAhFokCxZ17exP1lyrhGy?usp=sharing)
3. Zemberek Jar'ının bulunduğu dizinde aşağıdaki komutu çalıştırarak corpus'u train edip model elde ediyoruz.
```java
java -jar zemberek-full.jar TrainClassifier -i corpus -o corpus.model -lr 0.1 -ec 50
```
4. Oluşturduğumuz modeli /resources/datasets/ dizinine kopyalayarak projemizi çalışır hale getiriyoruz.

t### KULLANIM
How to use will be here...
