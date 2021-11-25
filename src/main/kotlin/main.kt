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
import com.ahs.camera.utils.launchType
import com.ahs.camera.utils.padding
import com.ahs.camera.utils.previewWidth
import com.ahs.camera.utils.windowWidth

fun main() = application {

    val data = remember { mutableStateOf(MessagesRepository.getMessages()) }
    val deviceStatus = remember { mutableStateOf(MessagesRepository.getDeviceStatus()) }
    val visible = remember { mutableStateOf(true) }
    val javacvPanel by lazy { JavacvCameraPanel(0) }
    val javacvPanel2 by lazy { JavacvCameraPanel(1) }
    val mvCamreraPanel by lazy { MvCamreraPanel() }

    Window(
        icon = painterResource("image/ic_icon.jpeg"),
        onCloseRequest = ::exitApplication,
        title = "拍照",
        resizable = false,
        state = rememberWindowState(width = windowWidth, height = 850.dp)
    ) {
        MaterialTheme {

            Column(Modifier.background(MaterialTheme.colors.surface).padding(padding)) {

                AnimatedVisibility(visible = visible.value) {
                    Column {
                        message(deviceStatus.value)
                        Row {
                            if (launchType == 1) {
                                cameraPreview(this@Window, mvCamreraPanel)
                            } else {
                                cameraPreview(this@Window, javacvPanel, padding)
                                cameraPreview(this@Window, javacvPanel2, 0.dp)
                            }
                        }
                    }
                }

                messageList(visible.value, data.value) { visible.value = !visible.value }
            }
        }
    }
}
