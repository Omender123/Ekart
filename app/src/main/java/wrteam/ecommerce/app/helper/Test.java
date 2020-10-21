package wrteam.ecommerce.app.helper;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.common.api.Status;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import kotlin.TypeCastException;
import kotlin.jvm.internal.Intrinsics;

public class Test extends BroadcastReceiver{

    private OTPListener otpReceiver;

        public final void injectOTPListener(@Nullable OTPListener receiver) {
            this.otpReceiver = receiver;
        }

        public void onReceive(@NotNull Context context, @NotNull Intent intent) {
            Intrinsics.checkParameterIsNotNull(context, "context");
            Intrinsics.checkParameterIsNotNull(intent, "intent");
            if (Intrinsics.areEqual("com.google.android.gms.auth.api.phone.SMS_RETRIEVED", intent.getAction())) {
                Bundle extras = intent.getExtras();
                Object var10000 = extras.get("com.google.android.gms.auth.api.phone.EXTRA_STATUS");
                if (var10000 == null) {
                    throw new TypeCastException("null cannot be cast to non-null type com.google.android.gms.common.api.Status");
                }

                Status status = (Status)var10000;
                String message = "=====status - '" + status + '\'';

                System.out.println(message);
                message = "=====extra - '" + extras + '\'';

                System.out.println(message);
                OTPListener var8;
                switch(status.getStatusCode()) {
                    case 0:
                        var10000 = extras.get("com.google.android.gms.auth.api.phone.EXTRA_SMS_MESSAGE");
                        if (var10000 == null) {
                            throw new TypeCastException("null cannot be cast to non-null type kotlin.String");
                        }

                        message = (String)var10000;
                        Pattern pattern = Pattern.compile("\\d{4}");
                        Matcher matcher = pattern.matcher((CharSequence)message);
                        if (matcher.find()) {
                            var8 = this.otpReceiver;
                            if (var8 != null) {
                                String var10001 = matcher.group(0);
                                Intrinsics.checkExpressionValueIsNotNull(var10001, "matcher.group(0)");
                                var8.onOTPReceived(var10001);
                            }

                            return;
                        }
                        break;
                    case 15:
                        var8 = this.otpReceiver;
                        if (var8 != null) {
                            var8.onOTPTimeOut();
                        }
                }
            }

        }


        public interface OTPListener {
            void onOTPReceived(@NotNull String var1);

            void onOTPTimeOut();
        }


}
