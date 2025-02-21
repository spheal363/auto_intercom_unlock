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

## Pi4Jのインストールする
- 参考：[JavaでRaspberry PiのGPIOを制御してLチカしてみる](https://reasonable-code.com/raspberrypi-java/)
```
$ curl -sSL https://pi4j.com/install | sudo bash
====================================================
INSTALLING Pi4J GPG PUBLIC KEY
====================================================
Warning: apt-key is deprecated. Manage keyring files in trusted.gpg.d instead (see apt-key(8)).
OK
====================================================
ADDING Pi4J APT REPOSITORY
====================================================
====================================================
UPDATING APT REPOSITORIES
====================================================
取得:1 https://www.pi4j.com/download v1 InRelease [3,840 B]
取得:2 https://www.pi4j.com/download v1/stable all Packages [889 B]
4,729 B を 1秒 で取得しました (3,334 B/s)
パッケージリストを読み込んでいます... 完了
W: https://pi4j.com/download/dists/v1/InRelease: Key is stored in legacy trusted.gpg keyring (/etc/apt/trusted.gpg), see the DEPRECATION section in apt-key(8) for details.
====================================================
INSTALLING Pi4J
====================================================
パッケージリストを読み込んでいます... 完了
依存関係ツリーを作成しています... 完了        
状態情報を読み取っています... 完了        
以下のパッケージが自動でインストールされましたが、もう必要とされていません:
  libwlroots12
これを削除するには 'sudo apt autoremove' を利用してください。
以下のパッケージが新たにインストールされます:
  pi4j
アップグレード: 0 個、新規インストール: 1 個、削除: 0 個、保留: 1 個。
579 kB のアーカイブを取得する必要があります。
この操作後に追加で 827 kB のディスク容量が消費されます。
取得:1 https://www.pi4j.com/download v1/stable all pi4j all 1.4 [579 kB]
579 kB を 1秒 で取得しました (496 kB/s)
以前に未選択のパッケージ pi4j を選択しています。
(データベースを読み込んでいます ... 現在 149141 個のファイルとディレクトリがインストールされています。)
.../apt/archives/pi4j_1.4_all.deb を展開する準備をしています ...
pi4j (1.4) を展開しています...
pi4j (1.4) を設定しています ...
====================================================
Pi4J INSTALLATION COMPLETE
====================================================

The Pi4J JAR files are located at:
/opt/pi4j/lib

Example Java programs are located at:
/opt/pi4j/examples

You can compile the examples using this script:
sudo /opt/pi4j/examples/build

Please see https://pi4j.com for more information.
```