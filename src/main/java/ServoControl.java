import com.pi4j.Pi4J;
import com.pi4j.context.Context;
import com.pi4j.io.pwm.Pwm;

public class ServoControl {

    private static final int SERVO_PIN = 18; // GPIO18 (BCM番号)

    public static void main(String[] args) throws InterruptedException {
        // Pi4Jコンテキストの初期化
        Context pi4j = Pi4J.newAutoContext();

        // PWM出力を設定
        Pwm pwm = pi4j.create(Pwm.newConfigBuilder(pi4j)
                .id("PWM_SERVO")
                .name("Servo PWM")
                .address(SERVO_PIN)
                .initial(0) // 初期値 0
                .frequency(50) // サーボ用の50Hz
                .build());

        // 使用例
        while (true) {
            setAngle(pwm, 90); // サーボを90度に設定
            Thread.sleep(1000);

            setAngle(pwm, 0); // サーボを0度に設定
            Thread.sleep(1000);

            setAngle(pwm, 180); // サーボを180度に設定
            Thread.sleep(1000);
        }
    }

    // サーボモーターを特定の角度に設定する関数
    private static void setAngle(Pwm pwm, int angle) {
        if (angle < 0 || angle > 180) {
            throw new IllegalArgumentException("角度は0から180の間でなければなりません");
        }

        // 角度を500から2500のパルス幅にマッピングする
        int dutyCycle = (int) ((angle / 180.0) * (2500 - 500) + 500);

        // PWM duty cycleを設定
        pwm.dutyCycle(dutyCycle);
    }
}
