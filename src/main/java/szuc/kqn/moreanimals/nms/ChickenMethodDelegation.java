package szuc.kqn.moreanimals.nms;


import net.bytebuddy.asm.Advice;
import net.bytebuddy.implementation.bind.annotation.*;


import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;
import java.util.function.Consumer;
import java.util.function.Function;

public class ChickenMethodDelegation {
    public static Consumer<Object> aiStep0=null;

    @Advice.OnMethodEnter
    public static void aiStep(@Advice.This Object thiz){
        if(aiStep0==null)return;
        aiStep0.accept(thiz);
    }
//    public static BiFunction<Object,Object,Boolean> isFood0=null;
//    @Advice.OnMethodExit
//    public static boolean isFood(@This Object thiz, @Argument(0) Object item) {
//        if(isFood0==null)return false;
//        else return isFood0.apply(thiz,item);
//    }

}
