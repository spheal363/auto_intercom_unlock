# ラズパイでのI2C設定

## I2Cインターフェースの有効化
```
$ sudo raspi-config
```
1. `3 Interface Options`を選択
![2024-12-18_17h28_48](https://github.com/user-attachments/assets/dfb1a6ce-5d4b-4070-9f7c-5936946b3c15)

2. `I5 I2C`を選択
![2024-12-18_17h29_00](https://github.com/user-attachments/assets/f8df0a25-4184-4e67-8e06-537599248cae)

3. `<はい>`を選択
![2024-12-18_17h29_08](https://github.com/user-attachments/assets/768f4e4c-259c-47b0-b7dd-42f21ba7f79e)
![2024-12-18_17h29_15](https://github.com/user-attachments/assets/0cb6fdfa-14ac-408f-896c-8982514edcbb)

## i2c-toolsのインストール
```
$ sudo apt-get install i2c-tools
```

## インストール確認
```
$ lsmod | grep i2c_dev
i2c_dev                16384  0
```