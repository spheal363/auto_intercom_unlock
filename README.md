# auto_intercom_unlock
自宅のインターホンが押されたことを検知してスマホから開錠

## 実行(maven)
```bash
~/auto_intercom_unlock/auto-intercom-app $ mvn clean package
```

```bash
sdoi@raspberrypi:~/auto_intercom_unlock/auto-intercom-app $ java -cp target/auto-intercom-app-1.0-SNAPSHOT.jar xyz.sdoi.App
SLF4J(W): No SLF4J providers were found.
SLF4J(W): Defaulting to no-operation (NOP) logger implementation
SLF4J(W): See https://www.slf4j.org/codes.html#noProviders for further details.
赤: 11 緑: 18 青: 8 総和: 37 緑割合: 0.4864864864864865
赤: 9 緑: 16 青: 7 総和: 32 緑割合: 0.5
赤: 8 緑: 14 青: 6 総和: 28 緑割合: 0.5
Response: {}
緑値が相対的に高いため通知を送信しました: 赤 6 緑 15 青 6 総和 27 緑割合 0.5555555555555556
赤: 12 緑: 21 青: 13 総和: 46 緑割合: 0.45652173913043476
赤: 11 緑: 21 青: 13 総和: 45 緑割合: 0.4666666666666667
赤: 11 緑: 22 青: 13 総和: 46 緑割合: 0.4782608695652174
```

## 実際の画面

- カラーセンサ
![2025-01-08_20h01_42](https://github.com/user-attachments/assets/2e6757e5-7346-4c44-add4-489e439228ec)
- LINE
![スクリーンショット 2024-12-14 192028](https://github.com/user-attachments/assets/8fa03418-8ae4-4f56-9589-4157bcc355d3)
