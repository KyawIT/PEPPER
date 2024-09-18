package at.htlleonding.pepperInElderlyCare.ui

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
//import at.htlleonding.pepperInElderlyCare.ui.HelpFunctions.Companion.touch
import at.htlleonding.pepperInElderlyCare.ui.navigation.ScreenNavigation
import at.htlleonding.pepperInElderlyCare.ui.theme.PepperInElderlyCareTheme
import com.aldebaran.qi.sdk.QiContext
import com.aldebaran.qi.sdk.QiSDK
import com.aldebaran.qi.sdk.RobotLifecycleCallbacks
import com.aldebaran.qi.sdk.design.activity.RobotActivity

@ExperimentalAnimationApi
class MainActivity : RobotActivity(), RobotLifecycleCallbacks {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        QiSDK.register(this, this)
        setContent {
            MyApp{
                ScreenNavigation()
            }
        }
    }
    override fun onRobotFocusGained(qiContext: QiContext?) {
        HelpFunctions.qIContext=qiContext
        //HelpFunctions.touch = qiContext?.touch
        //HelpFunctions.headTouchSensor = touch?.getSensor("Head/Touch")
    }

    override fun onRobotFocusLost() {
        QiSDK.unregister(this, this)
        super.onDestroy()
    }

    override fun onRobotFocusRefused(reason: String?) {
        TODO("Not yet implemented")
    }
}

@Composable
fun MyApp(content: @Composable () -> Unit) {
    if(HelpFunctions.onPepper)
    {
        while(HelpFunctions.qIContext==null){}
    }
    PepperInElderlyCareTheme {
        content()
    }
}
