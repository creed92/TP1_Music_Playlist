package com.example.tpmusic_list

import android.media.MediaMetadataRetriever
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.media.MediaPlayer
import android.view.View
import android.view.ViewGroup
import android.widget.*

class MusicItem(val title: String, val description: String, val imageId: Int, val musicResourceId: Int ,val duration: Int)

class MainActivity : AppCompatActivity() {

    private lateinit var musicList: List<MusicItem>
    private lateinit var musicListView: ListView
    private lateinit var musicImageView: ImageView
    private lateinit var musicTitleView: TextView
    private lateinit var mediaPlayer: MediaPlayer
    private var currentIndex = 0
    var israndom=false
    var starting =true
    var isPaused = false



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)



        //creating my metal music PL
        musicList = listOf(
            MusicItem("Hypnotize", "Soad", R.drawable.sh, R.raw.sh,200),
            MusicItem("Awaken", "Metalocalypse_Dethklok", R.drawable.aw, R.raw.aw,300),
            MusicItem("Links", "Rammstein", R.drawable.rl, R.raw.rl,400),
            MusicItem("Sulfur", "Slipknot", R.drawable.ss, R.raw.ss,500),
            MusicItem("The_Rumbling", "Sim", R.drawable.rs, R.raw.rs,600)
        )

        val pauseButton: Button = findViewById(R.id.Play)

        //setting up the adapter
        musicImageView = findViewById(R.id.Image)
        musicTitleView = findViewById(R.id.MusicTitle)
        musicListView = findViewById(R.id.MusicList)

        mediaPlayer = MediaPlayer.create(this, musicList[currentIndex].musicResourceId)

        val adapter = object : ArrayAdapter<MusicItem>(this, R.layout.layout2, musicList) {

            override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
                val view = convertView ?: layoutInflater.inflate(R.layout.layout2, parent, false)
                val musicItem = musicList[position]

                val image = view.findViewById<ImageView>(R.id.imageView)
                image.setImageResource(musicItem.imageId)

                val title = view.findViewById<TextView>(R.id.tvItem1)
                title.text = musicItem.title

                val desc = view.findViewById<TextView>(R.id.tvItem2)
                desc.text = musicItem.description

                return view
            }
        }
        musicListView.adapter = adapter


        //text lecture mode
        val textView: TextView = findViewById(R.id.looporand)
        textView.text = "Loop mode"



        //play music on click
        musicListView.setOnItemClickListener { parent, view, position, id ->
            currentIndex = position
            pauseButton.setBackgroundResource(R.drawable.pause);
            playMusic()
        }



        //random button
        val randomButton: Button = findViewById(R.id.Random)
        randomButton.setOnClickListener {
            israndom=true
            textView.text = "Random mode"

        }

        //loop button
        val loopButton: Button = findViewById(R.id.loop)
        loopButton.setOnClickListener {
            israndom=false
            textView.text = "Loop mode"

        }

        //next button
        val nextButton: Button = findViewById(R.id.Next)
        nextButton.setOnClickListener {
            if(israndom)
            {
                currentIndex = getRandomIndex()
                playMusic()
            }
            else if(!israndom)
            {
                if (currentIndex < musicList.size - 1) {
                    currentIndex++
                    playMusic()
                }
                else if(currentIndex == musicList.size-1){
                    currentIndex=0
                    playMusic()
                }
            }

        }


        //prev button
        val prevButton: Button = findViewById(R.id.Prec)
        prevButton.setOnClickListener {
            if(israndom)
            {
                currentIndex = getRandomIndex()
                playMusic()
            }
            else if(!israndom) {
                if (currentIndex > 0) {
                    currentIndex--
                    playMusic()
                } else if (currentIndex == 0) {
                    currentIndex = musicList.size - 1
                    playMusic()
                }
            }
        }


        //play and pause button
        pauseButton.setOnClickListener {
            if (mediaPlayer.isPlaying) {
                mediaPlayer.pause()
                isPaused = true
                pauseButton.setBackgroundResource(R.drawable.play);

            } else if (starting){
                pauseButton.setBackgroundResource(R.drawable.pause);
                    playMusic()
                }
                else {
                    mediaPlayer.start()
                    isPaused = false
                    pauseButton.setBackgroundResource(R.drawable.pause);
                }
        }

        //sort by title
        val sorttitlebutton: Button = findViewById(R.id.sortbytitle)
        sorttitlebutton.setOnClickListener{
        sortmusictitle()
        }

        //sort by artist
        val sortdescbutton: Button = findViewById(R.id.sortbyartist)
        sortdescbutton.setOnClickListener{
            sortmusicdescr()
        }

        //sort by duration
        val sortdurationbutton: Button = findViewById(R.id.sortbyduration)
        sortdurationbutton.setOnClickListener{
            sortmusicduration()
        }
    }

    //play music fun
    private fun playMusic() {
        starting=false
        mediaPlayer.stop()
        mediaPlayer.release()

        mediaPlayer = MediaPlayer.create(this, musicList[currentIndex].musicResourceId)
        mediaPlayer.start()

        musicImageView.setImageResource(musicList[currentIndex].imageId)
        musicTitleView.text = musicList[currentIndex].title

        mediaPlayer.setOnCompletionListener {
            if(israndom)
            {
                currentIndex = getRandomIndex()
                playMusic()
            }
            else if(!israndom)
            {
                if (currentIndex < musicList.size - 1) {
                    currentIndex++
                    playMusic()
                }
                else if(currentIndex == musicList.size-1){
                    currentIndex=0
                    playMusic()
                }
            }
        }
    }

    //random fun
    private fun getRandomIndex(): Int {
        return (0 until musicList.size).random()
    }


    //sort by title
    private fun sortmusictitle() {
        musicList = musicList.sortedBy { it.title }
        //update the list
        (musicListView.adapter as ArrayAdapter<MusicItem>).notifyDataSetChanged()
    }


    //sort by description
    private fun sortmusicdescr() {
        musicList= musicList.sortedBy { it.description }
        //update the list
        (musicListView.adapter as ArrayAdapter<MusicItem>).notifyDataSetChanged()
    }


    //sort by duration
    private fun sortmusicduration() {
        musicList= musicList.sortedBy { it.duration }
        //update the list
        (musicListView.adapter as ArrayAdapter<MusicItem>).notifyDataSetChanged()
    }

}
