package com.example.arcore32_round_firebase

import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.ar.core.Anchor
import com.google.ar.sceneform.AnchorNode
import com.google.ar.sceneform.Node
import com.google.ar.sceneform.animation.ModelAnimator
import com.google.ar.sceneform.assets.RenderableSource
import com.google.ar.sceneform.math.Quaternion
import com.google.ar.sceneform.math.Vector3
import com.google.ar.sceneform.rendering.ModelRenderable
import com.google.ar.sceneform.ux.ArFragment
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity() {


    lateinit var arFragment: ArFragment
    private lateinit var util: Util
    lateinit var model: Model
    var fireBaseSource = true
    var modelResourceId = 1
    var animationSring = ""
    var url = ""
    var speed=0f
    var rotateX = 0f
    var rotateY = 0f
    var rotateZ = 0f
    var radius = 0f
    var hight = 0f
    var scale = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        arFragment = fragment as ArFragment
        util = Util(this, arFragment)

        arFragment.setOnTapArPlaneListener { hitResult, _, _ ->
            Toast.makeText(this, "Get Click", Toast.LENGTH_LONG).show()

            // val randomId=R.raw.beedrill
            //  val randomId = R.raw.fly   //fly  beautiful bird without color
            // val randomId = R.raw.skeb   // red eagle
            val randomId = R.raw.phoenix   // phoenix


            if (fireBaseSource) {
                setValueFirebase(1)
                spawnObject(hitResult.createAnchor(), Uri.parse(url))
            } else {
                loadModelAndAddToScene(hitResult.createAnchor(), randomId)
            }
            util.eliminateDot()
            util.activateButtom()
        }
    }

    private fun fireBaseActivale() {
        arFragment.setOnTapArPlaneListener { hitResult, _, _ ->
            spawnObject(hitResult.createAnchor(), Uri.parse(url))
        }
    }

    private fun spawnObject(anchor: Anchor, modelUri: Uri) {
        val rendrebaleSource = RenderableSource.builder()
            .setSource(this, modelUri, RenderableSource.SourceType.GLB)
            .setScale(0.01f)
            .setRecenterMode(RenderableSource.RecenterMode.ROOT)
            .build()
        ModelRenderable.builder()
            .setSource(this, rendrebaleSource)
            .setRegistryId(modelUri)
            .build()
            .thenAccept {
                addNodeToSceneFirebase(anchor, it)
            }.exceptionally {
                Log.e("clima", "Somthing go wrong in loading model")
                null
            }
    }


    private fun loadModelAndAddToScene(ancore: Anchor, modelResourceId: Int) {
        ModelRenderable.builder()
            .setSource(this, modelResourceId)
            .build()
            .thenAccept {
                val spaceShip = when (modelResourceId) {

                    R.raw.beedrill -> Model.Bee
                    R.raw.fly -> Model.Fly  //fly  beautiful bird without color
                    R.raw.skeb -> Model.Skeb // red eagle
                    R.raw.phoenix -> Model.Phoenix // red eagle
                    else -> Model.Bee
                }
                addNodeToScene(ancore, it)
                Toast.makeText(this, "Loaded", Toast.LENGTH_LONG).show()

            }.exceptionally {
                Toast.makeText(this, "Error in loading Model", Toast.LENGTH_LONG).show()
                null
            }

    }

    private fun addNodeToSceneFirebase(anchor: Anchor, modR: ModelRenderable) {

       val anchorNode = AnchorNode(anchor)
        /*    val rotatingNode = RotatingNode(speed).apply {
               setParent(anchorNode)
           }
           Node().apply {
               renderable = modR
               setParent(rotatingNode)
               localScale = Vector3(scale, scale, scale)

               localPosition = Vector3(radius, hight, 0f)         //X,Y,Z

               // Y axis points Up !!!


               localRotation = Quaternion.eulerAngles(Vector3(rotateX, rotateY, rotateZ))
               startAnimation(renderable as ModelRenderable)
           }
           */

        val rotatingNode = RotatingNode(speed).apply {
            setParent(anchorNode)
        }
        Node().apply {
            renderable = modR
            setParent(rotatingNode)
            scale=30f
            radius=2f
           localScale = Vector3(scale, scale, scale)
                 localPosition = Vector3(radius, hight, 0f)         //X,Y,Z
             //    localRotation = Quaternion.eulerAngles(Vector3(rotateX, rotateY, rotateZ))
                 startAnimation(renderable as ModelRenderable)
        }

        /*TransformableNode(arFragment.transformationSystem).apply {
            renderable = modR
            setParent(anchorNode)
        }*/
        arFragment.arSceneView.scene.addChild(anchorNode)
        Toast.makeText(this, "Loded 32 Firbase", Toast.LENGTH_LONG).show()
    }

    private fun addNodeToScene(anchor: Anchor, modR: ModelRenderable) {
       setValue(4)
        val anchorNode = AnchorNode(anchor)
        val rotatingNode = RotatingNode(speed).apply {
            setParent(anchorNode)
        }
        Node().apply {
            renderable = modR
            setParent(rotatingNode)
            localScale = Vector3(scale, scale, scale)

            localPosition = Vector3(radius, hight, 0f)         //X,Y,Z

            // Y axis points Up !!!


            localRotation = Quaternion.eulerAngles(Vector3(rotateX, rotateY, rotateZ))
            startAnimation(renderable as ModelRenderable)
        }
        arFragment.arSceneView.scene.addChild(anchorNode)

    }

    private fun setValue(ind:Int){
        when (ind){
            1->{ //bee
                 speed=50f
                 rotateX=-60f
                 rotateY=0f
                 rotateZ=0f
            }
            2->{ //fly  beautiful bird without color
                 speed = 20f
                 rotateX = 0f
                 rotateY = 0f
                 rotateZ = 0f
            }
            3->{ //skeb  red eagle
                 speed = 40f
                 rotateX = 0f
                 rotateY = 0f
                 rotateZ = 0f
            }
            4->{ //phoanix
                speed = 10f
                rotateX = 0f
                rotateY = -90f
                rotateZ = 0f
                radius = 2f
                hight = 1.1f
                scale = 0.3f
            }
        }
    }
    private fun setValueFirebase(ind:Int){
        when (ind){
            1->{  model = Model.GoldenFish
                url = "https://firebasestorage.googleapis.com/v0/b/thermal-proton-239415" +
                        ".appspot.com/o/golde_fish.glb?alt=media&token=04c99487-dea7-48a0-96f5-05a2f1fed3d5"
                speed = 10f
                rotateX = 0f
                rotateY = -90f
                rotateZ = 0f
                radius = 2f
                hight = 0.7f
                scale = 0.3f
            }

        }
    }

    private fun startAnimation(renderable: ModelRenderable) {


        // val animationString="Beedrill_Animation"
        //  val animationString = "fly"
        // val animationString = "skeb"

        val animationString = "fly"


        if (renderable.animationDataCount == 0) {
            return
        }
        val animationData = renderable.getAnimationData(animationString)
        ModelAnimator(animationData, renderable).apply {
            repeatCount = ModelAnimator.INFINITE
            start()
        }
    }


}

