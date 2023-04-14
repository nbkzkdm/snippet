# 郵便番号を取得して更新する処理

日本郵便株式会社が公開している郵便番号と住所のCSVから、データを取得しH2DBに保存して、郵便番号検索できるようにしています。


## 郵便番号から住所を取得する処理

1. 郵便番号7桁の前方一致検索
2. 都道府県から市区町村の情報の取得
3. 市区町村名から町名の情報の取得
4. 郵便番号データの取得更新

## 1.郵便番号7桁の前方一致検索

``` shell
curl --location 'localhost:18080/postalcode/search/0010014'
```

### 郵便番号からの住所情報の取得response

``` json
{
    "message": "郵便番号取得",
    "data": [
        {
            "localGovernmentCode": "01102",
            "oldPostalCode": "001  ",
            "postalCode": "0010014",
            "prefectureNameHwK": "ﾎｯｶｲﾄﾞｳ",
            "cityTownVillageHwK": "ｻｯﾎﾟﾛｼｷﾀｸ",
            "streetNameHwK": "ｷﾀ14ｼﾞｮｳﾆｼ(1-4ﾁｮｳﾒ)",
            "prefectureName": "北海道",
            "cityTownVillage": "札幌市北区",
            "streetName": "北十四条西（１〜４丁目）"
        }
    ]
}
```

## 2.都道府県から市区町村の情報の取得

``` shell
curl --location 'localhost:18080/postalcode/prefecture/北海道'
```

### 市区町村の情報の取得response

``` json
{
    "message": "都道府県配下の市区町村データの取得",
    "data": {
        "prefectureName": "北海道",
        "cityTownVillageList": [
            {
                "localGovernmentCode": "01102",
                "prefectureNameHwK": "ﾎｯｶｲﾄﾞｳ",
                "cityTownVillageHwK": "ｻｯﾎﾟﾛｼｷﾀｸ",
                "prefectureName": "北海道",
                "cityTownVillage": "札幌市北区"
            },
            {
                "localGovernmentCode": "01303",
                "prefectureNameHwK": "ﾎｯｶｲﾄﾞｳ",
                "cityTownVillageHwK": "ｲｼｶﾘｸﾞﾝﾄｳﾍﾞﾂﾁｮｳ",
                "prefectureName": "北海道",
                "cityTownVillage": "石狩郡当別町"
            },
            ... <snip>
```

## 3.市区町村名から町名の情報の取得

``` shell
curl --location 'localhost:18080/postalcode/cityTownVillage/札幌市北区'
```

### 町名の情報の取得response

``` json
{
    "message": "市区町村配下の町名取得",
    "data": {
        "localGovernmentCode": "01102",
        "prefectureName": "北海道",
        "cityTownVillage": "札幌市北区",
        "streetNameList": [
            {
                "localGovernmentCode": "01102",
                "oldPostalCode": "001  ",
                "postalCode": "0010000",
                "prefectureNameHwK": "ﾎｯｶｲﾄﾞｳ",
                "cityTownVillageHwK": "ｻｯﾎﾟﾛｼｷﾀｸ",
                "streetNameHwK": "ｲｶﾆｹｲｻｲｶﾞﾅｲﾊﾞｱｲ",
                "prefectureName": "北海道",
                "cityTownVillage": "札幌市北区",
                "streetName": "以下に掲載がない場合"
            },
            ... <snip>
```

## 4.郵便番号データの取得更新

``` shell
curl --location --request POST 'localhost:18080/postalcode/update'
```

### 市区町村の情報の取得response

``` json
{
    "message": "最新データ取得",
    "data": 7
}
```

郵便番号データを公表されているデータから取得する

[読み仮名データの促音・拗音を小書きで表記するもの(zip形式)](https://www.post.japanpost.jp/zipcode/dl/kogaki-zip.html)
に記載の郵便番号データをダウンロードして、それを内部DBへ保存して差分を月一で更新する

参考：[郵便番号データの説明](https://www.post.japanpost.jp/zipcode/dl/readme.html)

### 処理の流れ

1. zipをダウンロード
2. zipを解凍
3. 文字コードをutf8に
4. csvを読み込み
5. DBへ登録
