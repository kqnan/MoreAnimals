package szuc.kqn.moreanimals.nms;

import com.mojang.datafixers.types.Func;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.implementation.bind.annotation.Argument;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.This;

import java.util.function.BiFunction;
import java.util.function.Function;

public class CowMethodDelegation {
    public static BiFunction<Object,Object, Function<Object,Object>> mobInteract0=null;
   /**
    * 在原方法进入前插入一段代码mobintercat。若mobintercat代码返回null，则执行原方法。否则跳过原方法，并在退出前把该段代码返回值作为原方法返回值
    * */
    @Advice.OnMethodEnter(skipOn = Advice.OnNonDefaultValue.class)
    public static Object mobInteract(@This Object thiz, @Argument(0) Object EntityHuman,@Argument(1) Object EnumHand) {
        if(mobInteract0==null)return null;
        return mobInteract0.apply(thiz,EntityHuman).apply(EntityHuman);
    }
    /**
     * enterValue表示从OnMethodEnter注解的方法返回的值
     * */
    @Advice.OnMethodExit
    static void exit(@Advice.Enter String enterValue,
                     @Advice.Return(readOnly = false) String ret) {
        if(enterValue!=null)ret = enterValue;
    }
}
