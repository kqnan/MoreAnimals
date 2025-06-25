package szuc.kqn.moreanimals.nms;

import net.bytebuddy.asm.Advice;

import java.util.function.Consumer;

public class CatMethodDelegation {
    public static Consumer<Object> giveMorningGift0=null;

    @Advice.OnMethodEnter(skipOn = Advice.OnNonDefaultValue.class)
    public static Object giveMorningGift(@Advice.This Object thiz) {
        System.out.println("ASdasdasdasd");
        giveMorningGift0.accept(thiz);
        return null;
    }
}
