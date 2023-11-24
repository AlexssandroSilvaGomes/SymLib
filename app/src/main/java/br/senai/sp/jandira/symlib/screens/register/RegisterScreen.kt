package br.senai.sp.jandira.symlib.screens.register

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.res.vectorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.LifecycleCoroutineScope
import br.senai.sp.jandira.symlib.R
import br.senai.sp.jandira.symlib.components.InputText
import br.senai.sp.jandira.symlib.repository.UserRepository
import br.senai.sp.jandira.symlib.ui.theme.SymLibTheme
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import coil.request.ImageRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import kotlinx.coroutines.launch

@Composable
fun RegisterScreen(lifecycleScope: LifecycleCoroutineScope) {

    //REFERENCIA PARA ACESSO E MANiPULACAO DO CLOUD STORAGE
    val storageRef: StorageReference = FirebaseStorage.getInstance().reference.child("teste")

    //REFERENCIA PARA ACESSO E MANIPULACAO DO CLOUD FIRESTORE
    val firebaseFirestore: FirebaseFirestore = FirebaseFirestore.getInstance()

    var emailState by remember {
        mutableStateOf("")
    }

    var passwordState by remember {
        mutableStateOf("")
    }

    var url by remember {
        mutableStateOf("")
    }

    val context = LocalContext.current

    var fotoUri by remember {
        mutableStateOf<Uri?>(null)
    }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) {
        fotoUri = it
    }

    var painter = rememberAsyncImagePainter(
        ImageRequest.Builder(context).data(fotoUri).build()
    )

    fun registerUser(
        email: String,
        senha: String,
        foto: Uri
    ) {

        val userRepository = UserRepository()

        lifecycleScope.launch {
            fotoUri?.let {

                val storageRefs: StorageReference =
                    FirebaseStorage.getInstance().reference.child("teste/${it}")

                val uploadTask = storageRefs.putFile(it)

                uploadTask
                    .addOnCompleteListener { task ->

                        if (task.isSuccessful) {
                            storageRefs.downloadUrl.addOnSuccessListener { uri ->
                                val map = HashMap<String, Any>()
                                map["pic"] = uri.toString()

                                firebaseFirestore
                                    .collection("images")
                                    .add(map)
                                    .addOnCompleteListener { firestoreTask ->
                                        if (firestoreTask.isSuccessful) {
                                            url = uri.toString()
                                        } else {
                                            Toast
                                                .makeText(
                                                    context,
                                                    "ERRO AO TENTAR REALIZAR O UPLOAD",
                                                    Toast.LENGTH_SHORT
                                                )
                                                .show()
                                        }
                                    }
                            }

                        } else {
                            Toast
                                .makeText(
                                    context,
                                    "ERRO AO TENTAR REALIZAR O UPLOAD",
                                    Toast.LENGTH_SHORT
                                )
                                .show()
                        }
                    }
            }
        }

        if (email != "" && senha != "" && url != "") {
            lifecycleScope.launch {
                val response = userRepository.registerUser(email, senha, url)

                if (response.isSuccessful) {
                    Toast
                        .makeText(
                            context,
                            "Usuario registrado com sucesso",
                            Toast.LENGTH_SHORT
                        )
                        .show()
                } else {
                    Toast
                        .makeText(
                            context,
                            "Usuario nao foi cadastrado",
                            Toast.LENGTH_SHORT
                        )
                        .show()
                }
            }
        }

    }

    SymLibTheme {
        Surface(
            modifier = Modifier
                .fillMaxSize(),
            color = Color.DarkGray
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = stringResource(id = R.string.register_title).uppercase(),
                    fontSize = 24.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )

                Column(
                    modifier = Modifier
                        .padding(16.dp, 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(50.dp)
                ) {
                    Box(
                        contentAlignment = Alignment.BottomEnd,
                        modifier = Modifier
                            .size(150.dp)
                            .clickable {
                                launcher.launch("image/*")
                            }
                    ) {
                        Surface(
                            modifier = Modifier
                                .fillMaxSize()
                                .align(Alignment.Center),
                            shape = CircleShape,
                            color = Color.Gray
                        ) {
                            Image(
                                painter = painter,
                                contentDescription = "",
                                modifier = Modifier
                                    .fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )

                        }
                        Image(
                            imageVector = ImageVector.vectorResource(id = R.drawable.baseline_camera_alt_24),
                            contentDescription = "",
                            modifier = Modifier
                                .height(28.dp)
                                .width(48.dp)
                                .align(Alignment.BottomEnd)
                                .padding(end = 16.dp)
                        )
                    }

                    InputText(
                        text = emailState,
                        placeholder = stringResource(id = R.string.placeholder_email),
                        onValueChange = { emailState = it }
                    )

                    InputText(
                        text = passwordState,
                        placeholder = stringResource(id = R.string.placeholder_password),
                        onValueChange = { passwordState = it }
                    )

                    Button(
                        onClick = {
                            lifecycleScope.launch {
                                registerUser(emailState, passwordState, fotoUri!!)
                            }
                        },
                        modifier = Modifier
                            .size(150.dp, 60.dp),
                        shape = RoundedCornerShape(10.dp),
                        colors = ButtonDefaults.buttonColors(Color.White)
                    ) {
                        Text(
                            text = stringResource(id = R.string.register_button),
                            fontSize = 18.sp,
                            color = Color.Black
                        )
                    }
                }
            }

        }
    }
}

//@Preview(showSystemUi = true, showBackground = true)
//@Composable
//fun PreviewRegisterScreen() {
//    RegisterScreen()
//}