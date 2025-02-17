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

## pigpioライブラリのインストール
```
$ sudo apt install pigpio
```

## pigpioデーモンの常時起動
```
$ sudo systemctl start pigpiod
$ sudo systemctl enable pigpiod
$ sudo systemctl status pigpiod
sdoi@raspberrypi:~ $ sudo systemctl status pigpiod
🟢 pigpiod.service - Daemon required to control GPIO pins via pigpio
     Loaded: loaded (/lib/systemd/system/pigpiod.service; enabled; vendor preset: enabled)
    Drop-In: /etc/systemd/system/pigpiod.service.d
             └─public.conf
     Active: active (running) since Mon 2025-02-17 19:15:28 JST; 2min 51s ago
    Process: 3739 ExecStart=/usr/bin/pigpiod (code=exited, status=0/SUCCESS)
   Main PID: 3740 (pigpiod)
      Tasks: 4 (limit: 949)
        CPU: 10.678s
     CGroup: /system.slice/pigpiod.service
             └─3740 /usr/bin/pigpiod

 2月 17 19:15:28 raspberrypi systemd[1]: Starting Daemon required to control GPIO pins via pigpio...
```
