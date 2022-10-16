package com.arabapps.hamaki.ui.fragment.lecture

import android.annotation.SuppressLint
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import android.bluetooth.BluetoothHeadset
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.pm.ActivityInfo
import android.content.res.Configuration
import android.content.res.Resources
import android.graphics.Color
import android.hardware.display.DisplayManager
import android.media.MediaRecorder
import android.media.PlaybackParams
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v4.media.session.MediaSessionCompat
import android.support.v4.media.session.PlaybackStateCompat
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.media.session.MediaButtonReceiver
import androidx.navigation.Navigation
import com.arabapps.hamaki.R
import com.arabapps.hamaki.adapter.AttachesAdapter
import com.arabapps.hamaki.databinding.LectureFragmentBinding
import com.arabapps.hamaki.helper.HtmlHelper
import com.arabapps.hamaki.ui.activity.login.LoginActivity
import com.google.android.exoplayer2.*
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory
import com.google.android.exoplayer2.source.ExtractorMediaSource
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.TrackGroupArray
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector
import com.google.android.exoplayer2.trackselection.TrackSelectionArray
import com.google.android.exoplayer2.trackselection.TrackSelector
import com.google.android.exoplayer2.ui.PlaybackControlView
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory
import com.google.android.exoplayer2.util.Util
import com.sasco.user.helper.SharedHelper
import kotlin.math.max
import kotlin.random.Random


private const val TAG = "LectureFragment"

class LectureFragment : Fragment(), ExoPlayer.EventListener {
    private var videoUrl: String = ""
    var simpleExoPlayer: SimpleExoPlayer? = null
    private var currentPosition: Long? = null
    private lateinit var viewModel: LectureViewModel
    var mediaSessionCompat: MediaSessionCompat? = null
    var builder: PlaybackStateCompat.Builder? = null


    lateinit var binding: LectureFragmentBinding
    private val broadcastReceiver2: BroadcastReceiver = object : BroadcastReceiver() {
        var device: BluetoothDevice? = null
        override fun onReceive(context: Context, intent: Intent) {
            val action = intent.action
            device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE)
            if (BluetoothAdapter.ACTION_STATE_CHANGED.equals(action)) {
                if (intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, -1)
                    == BluetoothAdapter.STATE_OFF
                ) {
                    // Bluetooth is disconnected, do handling here
                    Log.d(TAG, "onReceive: Bluetooth is disconnected")
                    isHeadset = 0
                    binding.root.context.let {
                        binding.viewBlock.visibility = View.VISIBLE
                        binding.toastLayout.visibility = View.VISIBLE
                        binding.videoView?.player?.playWhenReady = false

                    }
                } else {
                    isHeadset = 1
                    binding.viewBlock.visibility = View.GONE
                    binding.toastLayout.visibility = View.GONE
                }
            }
        }
    }

    @SuppressLint("MissingPermission")
    fun isBluetoothHeadsetConnected(): Boolean {
        val mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
        return (mBluetoothAdapter != null && mBluetoothAdapter.isEnabled
                && mBluetoothAdapter.getProfileConnectionState(BluetoothHeadset.HEADSET) == BluetoothHeadset.STATE_CONNECTED)
    }

    var isHeadset = -1
    val br: BroadcastReceiver = broadcastReceiver()

    inner class broadcastReceiver : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val action = intent?.action
            var iii = 2
            if (Intent.ACTION_HEADSET_PLUG == action) {
                iii = intent.getIntExtra("state", -1)
                if (Integer.valueOf(iii) == 0) {
                    isHeadset = 0
                    binding.root.context.let {
                        binding.toastLayout.visibility = View.VISIBLE
                        binding.viewBlock.isVisible = true
                        binding.videoView?.player?.playWhenReady = false
                    }
                    if (isBluetoothHeadsetConnected()) {
                        isHeadset = 1
                        binding.viewBlock.visibility = View.GONE
                        binding.toastLayout.visibility = View.GONE
                    } else {
                        isHeadset = 0
                        binding.root.context.let {
                            binding.viewBlock.visibility = View.VISIBLE
                            binding.toastLayout.visibility = View.VISIBLE
                            binding.videoView?.player?.playWhenReady = false

                        }
                    }

                }
                if (Integer.valueOf(iii) == 1) {
                    isHeadset = 1
                    binding.viewBlock.isVisible = false
                    binding.toastLayout.visibility = View.GONE
                }
            }
        }

    }


    val fullscreenBroadcast = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            if (resources.configuration.orientation != Configuration.ORIENTATION_LANDSCAPE) {

                binding.textMainTitle.visibility = View.VISIBLE
                binding.textDesc.visibility = View.VISIBLE
                binding.attachsReycler.visibility = View.VISIBLE
                binding.textView14.visibility = View.VISIBLE
                binding.view.visibility = View.VISIBLE
                binding.textTitle.visibility = View.VISIBLE
                binding.instructions.visibility = View.VISIBLE
                val layoutParams: ViewGroup.LayoutParams =
                    binding.player.layoutParams
                layoutParams.height = dpToPx(240)
                binding.player.layoutParams = layoutParams
            } else {
                binding.textMainTitle.visibility = View.INVISIBLE
                binding.textDesc.visibility = View.INVISIBLE
                binding.attachsReycler.visibility = View.INVISIBLE
                binding.textView14.visibility = View.INVISIBLE
                binding.view.visibility = View.INVISIBLE
                binding.textTitle.visibility = View.INVISIBLE
                binding.instructions.visibility = View.INVISIBLE

                val layoutParams: ViewGroup.LayoutParams =
                    binding.player.layoutParams
                layoutParams.width = ViewGroup.LayoutParams.MATCH_PARENT
                layoutParams.height = ViewGroup.LayoutParams.MATCH_PARENT
                binding.player.layoutParams = layoutParams
            }
        }
    }


    fun dpToPx(dp: Int): Int {
        return (dp * Resources.getSystem().displayMetrics.density).toInt()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        currentPosition = savedInstanceState?.getLong("pos")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = LectureFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }


    val updateBroadcast = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            adapter.notifyDataSetChanged()

        }

    }

    val reciver = updateBroadcast
    val adapter = AttachesAdapter()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val receiverFilter = IntentFilter(Intent.ACTION_HEADSET_PLUG)
        binding.root.context.registerReceiver(br, receiverFilter)
        binding.root.context.registerReceiver(
            broadcastReceiver2,
            IntentFilter(BluetoothAdapter.ACTION_STATE_CHANGED)
        );
        if (context?.let { SharedHelper.getString(it, "ScreenRecord") } == "true") {
            binding.viewBlock2.visibility = View.VISIBLE
            binding.textScreenRecord.visibility = View.VISIBLE
        } else {
            binding.viewBlock2.visibility = View.GONE
            binding.textScreenRecord.visibility = View.GONE
        }
        //disable audio record
        val listener = object : DisplayManager.DisplayListener {
            override fun onDisplayChanged(displayId: Int) {

            }

            override fun onDisplayAdded(displayId: Int) {
                 binding.videoView?.player?.playWhenReady = false
                binding.viewBlock2.visibility = View.VISIBLE
                binding.textScreenRecord.visibility = View.VISIBLE
            }

            override fun onDisplayRemoved(displayId: Int) {
                binding.viewBlock2.visibility = View.GONE
                binding.textScreenRecord.visibility = View.GONE
            }
        }

        val displayManager = context?.getSystemService(Context.DISPLAY_SERVICE) as? DisplayManager
        displayManager?.registerDisplayListener(listener, null)
        binding.attachsReycler.adapter = adapter
        viewModel = ViewModelProvider(this).get(LectureViewModel::class.java)
        binding.root.context.registerReceiver(reciver, IntentFilter("ACTION_UPDATE_FILES"))
        binding.instructions.setOnClickListener {
            Navigation.createNavigateOnClickListener(R.id.action_lectureFragment_to_instructionsFragment)
                .onClick(it)
        }


        viewModel.loginLiveData.observe(viewLifecycleOwner, Observer {
            if (view == null)
                return@Observer

            if (it == null && context?.let { it1 ->
                    SharedHelper.getString(it1, SharedHelper.TOKEN)
                        .isNullOrEmpty()
                }!!
            ) {
                val intent = Intent(context, LoginActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                startActivity(intent)
                activity?.finish()
            } else if (it != null) {
                binding.apply {
                    adapter.setAll(it.files)
                    initializePlayer(it.video480)
                    videoUrl = "480"
                    binding.p480.setOnClickListener { view ->

                        if (it.video480.isNullOrEmpty()) {
                            Toast.makeText(context, "Quality not available", Toast.LENGTH_SHORT)
                                .show()
                            return@setOnClickListener
                        }
                        videoUrl = "480"
                        binding.p720.setTextColor(if (videoUrl.contentEquals("720")) Color.WHITE else Color.GRAY)
                        binding.p480.setTextColor(if (videoUrl.contentEquals("480")) Color.WHITE else Color.GRAY)
                        initializePlayer(it.video480)
                    }
                    binding.p720.setOnClickListener { view ->

                        if (it.video720.isNullOrEmpty()) {
                            Toast.makeText(context, "Quality not available", Toast.LENGTH_SHORT)
                                .show()
                            return@setOnClickListener
                        }
                        videoUrl = "720"
                        binding.p720.setTextColor(if (videoUrl.contentEquals("720")) Color.WHITE else Color.GRAY)
                        binding.p480.setTextColor(if (videoUrl.contentEquals("480")) Color.WHITE else Color.GRAY)
                        initializePlayer(it.video720)
                    }
                    binding.textMainTitle.text = it.name
                    binding.textDesc.text = HtmlHelper.getString(it.description.toString())
                    binding.textTitle.text = it.name
                }
            }
        })

        binding.root.context.registerReceiver(
            fullscreenBroadcast,
            IntentFilter("action_landscape")
        )

    }

    override fun onStart() {
        super.onStart()
        arguments?.getInt("id")?.let { it1 -> viewModel.lectureByID(binding.root.context, it1) }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putLong("pos", binding.videoView.player.currentPosition ?: 0)
    }

    @SuppressLint("SetTextI18n")
    private fun initializePlayer(videoUrl: String?) {


        binding.videoView.player?.stop()
        binding.videoView.player?.release()
        aniumateText()
        binding.tempMessage.text =
            SharedHelper.getString(binding.root.context, SharedHelper.NAME) + "\n" +
                    SharedHelper.getString(binding.root.context, SharedHelper.PHONE)
        videoUrl?.let {
            mediaSessionCompat = MediaSessionCompat(binding.root.context, TAG)
            mediaSessionCompat?.setFlags(
                MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS or
                        MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS
            )
            mediaSessionCompat?.setMediaButtonReceiver(null)
            builder = PlaybackStateCompat.Builder()
                .setActions(
                    PlaybackStateCompat.ACTION_PLAY or
                            PlaybackStateCompat.ACTION_PAUSE
                )
            mediaSessionCompat?.setPlaybackState(builder?.build())
            mediaSessionCompat?.setCallback(Mediasessioncall())
            mediaSessionCompat?.isActive = true

            val trackSelector: TrackSelector = DefaultTrackSelector()
            val loadControl: LoadControl = DefaultLoadControl()
            simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(
                binding.root.context,
                trackSelector,
                loadControl
            )
            binding.fullscreen.setOnClickListener {
                if (resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE) {
                    activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
                } else
                    activity?.requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
            }
            binding.settings.setOnClickListener {
                binding.linearSpeed.isVisible = true
                binding.linearQuality.isVisible = true
                binding.p720.setTextColor(if (this.videoUrl.contentEquals("720")) Color.WHITE else Color.GRAY)
                binding.p480.setTextColor(if (this.videoUrl.contentEquals("480")) Color.WHITE else Color.GRAY)
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                    when (simpleExoPlayer?.playbackParams?.speed) {
                        .5f -> {
                            binding.apply {
                                x5.setTextColor(Color.parseColor("#FFFFFF"))
                                x1.setTextColor(Color.parseColor("#999999"))
                                x15.setTextColor(Color.parseColor("#999999"))
                                x2.setTextColor(Color.parseColor("#999999"))
                            }
                        }

                        1.5f -> {
                            binding.apply {
                                x15.setTextColor(Color.parseColor("#FFFFFF"))
                                x5.setTextColor(Color.parseColor("#999999"))
                                x1.setTextColor(Color.parseColor("#999999"))
                                x2.setTextColor(Color.parseColor("#999999"))
                            }
                        }
                        2f -> {
                            binding.apply {
                                x2.setTextColor(Color.parseColor("#FFFFFF"))
                                x5.setTextColor(Color.parseColor("#999999"))
                                x15.setTextColor(Color.parseColor("#999999"))
                                x1.setTextColor(Color.parseColor("#999999"))
                            }
                        }
                        else -> {
                            binding.apply {
                                x1.setTextColor(Color.parseColor("#FFFFFF"))
                                x5.setTextColor(Color.parseColor("#999999"))
                                x15.setTextColor(Color.parseColor("#999999"))
                                x2.setTextColor(Color.parseColor("#999999"))
                            }
                        }
                    }
                }
            }
            binding.x1.setOnClickListener {
                var param: PlaybackParams? = null
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    param = PlaybackParams()
                    param.speed = 1f // 1f is 1x, 2f is 2x
                    simpleExoPlayer?.playbackParams = param
                }
                binding.videoView.hideController()
            }
            binding.x2.setOnClickListener {
                var param: PlaybackParams? = null
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    param = PlaybackParams()
                    param?.speed = 2f // 1f is 1x, 2f is 2x
                    simpleExoPlayer?.playbackParams = param
                }
                binding.videoView.hideController()
            }
            binding.x15.setOnClickListener {
                var param: PlaybackParams? = null
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    param = PlaybackParams()
                    param?.speed = 1.5f // 1f is 1x, 2f is 2x
                    simpleExoPlayer?.playbackParams = param
                }
                binding.videoView.hideController()
            }
            binding.x5.setOnClickListener {
                var param: PlaybackParams? = null
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    param = PlaybackParams()
                    param?.speed = .5f // 1f is 1x, 2f is 2x
                    simpleExoPlayer?.playbackParams = param
                }
                binding.videoView.hideController()
            }
            binding.videoView.player = simpleExoPlayer
            simpleExoPlayer?.addListener(this)
            val userAgent = Util.getUserAgent(binding.root.context, TAG)
            val mediaSource: MediaSource = ExtractorMediaSource(
                Uri.parse(videoUrl), DefaultDataSourceFactory(
                    binding.root.context, userAgent
                ), DefaultExtractorsFactory(), null, null
            )
            val playbackControlView: PlaybackControlView? =
                findPlaybackControlView(binding.videoView)
            playbackControlView?.setVisibilityListener {
                binding.settings.isVisible = (it == 0)
                binding.fullscreen.isVisible = (it == 0)
                if (binding.linearSpeed.isVisible) {
                    binding.linearSpeed.isVisible = (it == 0)
                    binding.linearQuality.isVisible = (it == 0)
                }
            }
            simpleExoPlayer?.prepare(mediaSource)
            simpleExoPlayer?.playWhenReady = false

        }
    }

    private fun findPlaybackControlView(viewGroup: ViewGroup): PlaybackControlView? {
        for (i in 0 until viewGroup.childCount) {
            val child = viewGroup.getChildAt(i)
            if (child is PlaybackControlView) return child
            if (child is ViewGroup) {
                val result = findPlaybackControlView(child)
                if (result != null) return result
            }
        }
        return null
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        activity?.window?.setFlags(
            WindowManager.LayoutParams.FLAG_SECURE,
            WindowManager.LayoutParams.FLAG_SECURE
        )
        binding.imageBack.setOnClickListener { activity?.onBackPressed() }

    }


    override fun onStop() {
        super.onStop()
        binding.videoView.player.stop()

    }

    public override fun onDestroy() {
        super.onDestroy()
        try {
            context?.unregisterReceiver(reciver)
            context?.unregisterReceiver(br)
            context?.unregisterReceiver(fullscreenBroadcast)
            binding.videoView.player.release()
        } catch (ex: Exception) {
        }


    }

    override fun onTimelineChanged(timeline: Timeline?, manifest: Any?) {
    }


    override fun onTracksChanged(
        trackGroups: TrackGroupArray?,
        trackSelections: TrackSelectionArray?
    ) {
    }


    private fun aniumateText() {
        val x = binding.root.width - binding.tempMessage.width
        val y = binding.root.height - binding.tempMessage.height
        val xRandom = Random.nextInt(max(Math.abs(x), 100))
        val yRandom = Random.nextInt(max(Math.abs(y), 100))
        try {


            activity?.runOnUiThread {
                binding.tempMessage.x = xRandom.toFloat()
                binding.tempMessage.y = yRandom.toFloat()
                binding.tempMessage.visibility = ViewGroup.VISIBLE
                Handler(Looper.getMainLooper()).postDelayed(Runnable {
                    activity?.runOnUiThread {
                        binding.tempMessage.visibility = ViewGroup.GONE
                        Handler(Looper.getMainLooper()).postDelayed(Runnable {


                            aniumateText()


                        }, 3000)

                    }

                }, 7000)

            }
        } catch (ex: Exception) {
            ex.printStackTrace()
        }
    }

    override fun onLoadingChanged(isLoading: Boolean) {
        binding.progress.isVisible = isLoading
    }

    override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
    }


    override fun onPlayerError(error: ExoPlaybackException?) {
    }

    override fun onPositionDiscontinuity() {
    }


    inner class Mediasessioncall : MediaSessionCompat.Callback() {
        override fun onPlay() {
            super.onPlay()
            simpleExoPlayer?.playWhenReady = true
        }

        override fun onPause() {
            super.onPause()
            simpleExoPlayer?.playWhenReady = false
        }
    }

    inner class Reciever : BroadcastReceiver() {
        override fun onReceive(context: Context, intent: Intent) {
            MediaButtonReceiver.handleIntent(mediaSessionCompat, intent)
        }
    }
}