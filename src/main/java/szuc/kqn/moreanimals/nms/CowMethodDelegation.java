package szuc.kqn.moreanimals.nms;

import com.mojang.datafixers.types.Func;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.implementation.bind.annotation.Argument;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.This;
import net.minecraft.world.EnumInteractionResult;

import java.util.function.BiFunction;
import java.util.function.Function;

public class CowMethodDelegation {
    public static BiFunction<Object,Object, Function<Object,Object>> mobInteract0=null;
   /**
    * 在原方法进入前插入一段代码mobintercat。若mobintercat代码返回null，则执行原方法。否则跳过原方法，并在退出前把该段代码返回值作为原方法返回值
    * */
    @Advice.OnMethodEnter(skipOn = Advice.OnNonDefaultValue.class)
    public static Object mobInteract(@Advice.This Object thiz, @Advice.Argument(0) Object EntityHuman,@Advice.Argument(value = 1) Object EnumHand) {
        //System.out.println(mobInteract0==null);
        if(mobInteract0==null)return null;//不跳过方法
        //不跳过方法
        return mobInteract0.apply(thiz, EntityHuman).apply(EnumHand);

    }
    /**
     * enterValue表示从OnMethodEnter注解的方法返回的值
     * */

    @Advice.OnMethodExit
    public static void exit(@Advice.Enter Object enterValue, @Advice.Return(readOnly = false) EnumInteractionResult ret) {
        if(enterValue!=null)ret = (EnumInteractionResult) enterValue;
    }
}
