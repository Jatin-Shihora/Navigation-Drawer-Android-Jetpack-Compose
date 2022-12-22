// Code For Navigation Drawer in Android Jetpack Compose

// Please replace the name of
// package with your project name
package com.example.navigationdrawer

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.DrawerValue
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ModalDrawer
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch

class DrawerAppActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // This sets the @Composable function as the
        // root view of the activity.
        // This is meant to replace the .xml file that
        // we would typically set using the setContent(R.id.xml_file) method.
        // The setContent block defines the activity's layout.
        setContent {
            DrawerAppComponent()
        }
    }
}

@Composable
fun DrawerAppComponent() {
    // Reacting to state changes is the core behavior of Compose
    // @remember helps to calculate the value passed to it only
    // during the first composition. It then
    // returns the same value for every subsequent composition.
    // @mutableStateOf as an observable value where updates to
    // this variable will redraw all
    // the composable functions. "only the composable
    // that depend on this will be redraw while the
    // rest remain unchanged making it more efficient".
    val drawerState = rememberDrawerState(DrawerValue.Closed)
    // State composable used to hold the
    // value of the current active screen
    val currentScreen = remember { mutableStateOf(DrawerAppScreen.Screen1) }

    val coroutineScope = rememberCoroutineScope()

    ModalDrawer(
        // Drawer state indicates whether
        // the drawer is open or closed.
        drawerState = drawerState,
        gesturesEnabled = drawerState.isOpen,
        drawerContent = {
            //drawerContent accepts a composable to represent
            // the view/layout that will be displayed
            // when the drawer is open.
            DrawerContentComponent(
                // We pass a state composable that represents the current screen that's selected
                // and what action to take when the drawer is closed.
                currentScreen = currentScreen,
                closeDrawer = { coroutineScope.launch { drawerState.close() } }
            )
        },
        content = {
            // bodyContent takes a composable to
            // represent the view/layout to display on the
            // screen. We select the appropriate screen
            // based on the value stored in currentScreen.
            BodyContentComponent(
                currentScreen = currentScreen.value,
                openDrawer = {
                    coroutineScope.launch { drawerState.open() }
                }
            )
        }
    )
}

@Composable
fun DrawerContentComponent(
    currentScreen: MutableState<DrawerAppScreen>,
    closeDrawer: () -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        // We want to have 3 rows in this column to represent the 3 screens in this activity.
        for (index in DrawerAppScreen.values().indices) {
            // Box with clickable modifier wraps the
            // child composable and enables it to react to a
            // click through the onClick callback similar
            // to the onClick listener that we are
            // accustomed to on Android.
            // Here, we just update the currentScreen variable
            // to hold the appropriate value based on
            // the row that is clicked i.e if the first
            // row is clicked, we set the value of
            // currentScreen to DrawerAppScreen.Screen1,
            // when second row is clicked we set it to
            // DrawerAppScreen.Screen2 and so on and so forth.
            val screen = getScreenBasedOnIndex(index)
            Column(Modifier.clickable(onClick = {
                currentScreen.value = screen
                // We also close the drawer when an
                // option from the drawer is selected.
                closeDrawer()
            }), content = {
                // bodyContent takes a composable to
                // represent the view/layout to display on the
                // screen.
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    // We set the color of the row based on whether
                    // that row represents the current
                    // screen that's selected. We only want to
                    // highlight the row that's selected.
                    color = if (currentScreen.value == screen) {
                        MaterialTheme.colors.secondary
                    } else {
                        MaterialTheme.colors.surface
                    }
                ) {
                    Text(text = screen.name, modifier = Modifier.padding(16.dp))
                }
            })
        }
    }
}

/**
 * Returns the corresponding DrawerAppScreen based on the index passed to it.
 */
fun getScreenBasedOnIndex(index: Int) = when (index) {
    0 -> DrawerAppScreen.Screen1
    1 -> DrawerAppScreen.Screen2
    2 -> DrawerAppScreen.Screen3
    else -> DrawerAppScreen.Screen1
}

/**
 * Passed the corresponding screen composable based on the current screen that's active.
 */
@Composable
fun BodyContentComponent(
    currentScreen: DrawerAppScreen,
    openDrawer: () -> Unit
) {
    when (currentScreen) {
        DrawerAppScreen.Screen1 -> Screen1Component(
            openDrawer
        )
        DrawerAppScreen.Screen2 -> Screen2Component(
            openDrawer
        )
        DrawerAppScreen.Screen3 -> Screen3Component(
            openDrawer
        )
    }
}

@Composable
fun Screen1Component(openDrawer: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize()) {
        // TopAppBar has slots for a title, navigation icon,
        // and actions. Also known as the action bar.
        TopAppBar(
            title = { Text("Screen 1 Title") },
            navigationIcon = {
                IconButton(onClick = openDrawer) {
                    Icon(imageVector = Icons.Filled.Menu, contentDescription = "Menu")
                }
            }
        )
        Surface(color = Color(0xFFffd7d7.toInt()), modifier = Modifier.weight(1f)) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                content = {
                    Text(text = "Geeks for geeks : Geeks learning from geeks")
                }
            )
        }
    }
}

@Composable
fun Screen2Component(openDrawer: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("Screen 2 Title") },
            navigationIcon = {
                IconButton(onClick = openDrawer) {
                    Icon(imageVector = Icons.Filled.Menu, contentDescription = "Menu")
                }
            }
        )
        Surface(color = Color(0xFFffe9d6.toInt()), modifier = Modifier.weight(1f)) {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                content = {
                    Text(text = "GFG : GeeksforGeeks was founded by Sandeep Jain")
                }
            )
        }
    }
}

@Composable
fun Screen3Component(openDrawer: () -> Unit) {
    Column(modifier = Modifier.fillMaxSize()) {
        TopAppBar(
            title = { Text("Screen 3 Title") },
            navigationIcon = {
                IconButton(onClick = openDrawer) {
                    Icon(imageVector = Icons.Filled.Menu, contentDescription = "Menu")
                }
            }
        )
        Surface(color = Color(0xFFfffbd0.toInt()), modifier = Modifier.weight(1f)) {
            Column(modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                content = {
                    Text(text = "Address: A-143, 9th Floor, Sovereign Corporate Tower Sector-136, Noida, Uttar Pradesh - 201305")
                }
            )
        }
    }
}

/**
 * Creating an enum class for ModelDrawer screens
 */
enum class DrawerAppScreen {
    Screen1,
    Screen2,
    Screen3
}

/**
 * Significance of @preview and composable annotations :
 */
@Preview
@Composable
fun DrawerAppComponentPreview() {
    DrawerAppComponent()
}