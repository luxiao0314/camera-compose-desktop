import androidx.compose.animation.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.*
import com.ahs.camera.model.MessagesRepository
import com.ahs.camera.ui.*
import com.ahs.camera.utils.padding
import com.ahs.camera.utils.previewWidth

fun main() = application {

    val data = remember { mutableStateOf(MessagesRepository.getMessages()) }
    val deviceStatus = remember { mutableStateOf(MessagesRepository.getDeviceStatus()) }
    val visible = remember { mutableStateOf(true) }
    val javacvPanel by lazy { JavacvCameraPanel() }
    val mvCamreraPanel by lazy { MvCamreraPanel() }

    Window(
        icon = painterResource("image/ic_icon.jpeg"),
        onCloseRequest = ::exitApplication,
        title = "拍照",
        resizable = false,
        state = rememberWindowState(width = Dp(previewWidth * 2.toFloat()) + padding * 4, height = 800.dp)
    ) {
        MaterialTheme {

            Column(Modifier.background(MaterialTheme.colors.surface).fillMaxSize().padding(padding)) {

                AnimatedVisibility(visible = visible.value) {
                    Column {
                        message(deviceStatus.value)
                        Row {
                            cameraPreview(this@Window, mvCamreraPanel)
                            cameraPreview(this@Window, javacvPanel)
                        }
                    }
                }

                messageList(visible.value, data.value) { visible.value = !visible.value }
            }
        }
    }
}
