package szuc.kqn.moreanimals.nms;

import net.bytebuddy.asm.Advice;
import net.bytebuddy.implementation.bind.annotation.Argument;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.This;

import java.util.Map;
import java.util.function.BiFunction;

public class ChickenMethodDelegationIsFood {
   // public static BiFunction<Object,Object, Map.Entry<Boolean,Boolean>> isFood0=null;
//    @Advice.OnMethodExit
//    public static void isFood(@Advice.Return(readOnly = false) boolean returnValue, @Advice.This Object thiz, @Advice.Argument(0) Object item) {
//        if(isFood0==null)return ;
//        Map.Entry<Boolean,Boolean> res= isFood0.apply(thiz,item);
//        System.out.println("raw:"+returnValue);
//        System.out.println(res.getKey()+" : "+res
//                .getValue());
//        System.out.println(item);
//        if(res.getKey()) {
//            return ;
//        }//返回原方法返回值
//        else returnValue=res.getValue();//rawReturn= res.getValue();//返回代理方法返回值
//    }

    public static BiFunction<Object,Object,Boolean> isFood0=null;

    @RuntimeType
    public static boolean isFood(@This Object thiz, @Argument(0) Object item) {
        if(isFood0==null)return false;
        else return isFood0.apply(thiz,item);
    }
}
