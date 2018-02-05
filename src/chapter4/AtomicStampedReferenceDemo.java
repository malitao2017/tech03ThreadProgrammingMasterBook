package chapter4;

import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * Created by 13 on 2017/5/6.
 */
public class AtomicStampedReferenceDemo {
    static AtomicStampedReference<Integer> money = new AtomicStampedReference<Integer>(19, 0);

    public static void main(String args[]) {

        for (int i = 0; i < 100; i++) {
        	//这里的final是关键，只有第一份的 0 ，以后不可以改变
            final int timestap = money.getStamp();
            System.out.println("-----关键信息设置为final："+timestap);
            new Thread() {
                public void run() {
                    while (true) {
                        Integer m = money.getReference();
                        if (m < 20) {
                            if (money.compareAndSet(m, m + 20, timestap, timestap + 1)) {
                                System.out.println("余额小于20元,充值成功,余额:" + money.getReference() + "元");
                                break;
                            }
                        } else {
                            System.out.println("余额大于20,无需充值");
                            break;
                        }
                    }
                }
            }.start();
        }

        new Thread() {
            public void run() {
                for (int i = 0; i < 100; i++) {

                    while (true) {
                        int timestap = money.getStamp();
                        System.out.println("花销："+timestap);
                        Integer m = money.getReference();
                        if (m > 10) {
                            System.out.println("金额大于10元");
                            if (money.compareAndSet(m, m - 10, timestap, timestap + 1)) {
                                System.out.println("成功消费10元,余额:" + money.getReference() + "元");
                                break;
                            }
                        } else {
                            System.out.println("没有足够的金额");
                            break;
                        }
                    }
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }.start();

    }
}
