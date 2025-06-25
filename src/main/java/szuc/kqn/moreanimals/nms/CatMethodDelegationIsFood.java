package szuc.kqn.moreanimals.nms;

import jdk.javadoc.internal.doclets.toolkit.util.Utils;
import net.bytebuddy.asm.Advice;
import net.bytebuddy.implementation.bind.annotation.Argument;
import net.bytebuddy.implementation.bind.annotation.RuntimeType;
import net.bytebuddy.implementation.bind.annotation.This;
import net.minecraft.world.EnumInteractionResult;

import java.util.function.BiFunction;
import java.util.function.Function;

public class CatMethodDelegationIsFood {
    public static BiFunction<Object,Object,Boolean> isFood0=null;

    @RuntimeType
    public static boolean isFood(@This Object thiz, @Argument(0) Object item) {
        if(isFood0==null)return false;
        else return isFood0.apply(thiz,item);
    }
}
