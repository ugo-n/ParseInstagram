package com.example.parseinstagram

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.parse.FindCallback
import com.parse.ParseException
import com.parse.ParseQuery
import com.parse.ParseUser

/**
 * Let user create a post by taking a photo with their camera
 */

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //1. Setting description of the post
        //2. A button to launch the camera to take a picture
        //3. An ImageView to show the picture the user has taken
        //4. A button to save and send the post to our Parse Server

        findViewById<Button>(R.id.btn_submit).setOnClickListener{
            // send post to server without image
            // get description that they have inputted
            val description = findViewById<EditText>(R.id.et_description).text.toString()
            val user = ParseUser.getCurrentUser()
            submitPost(description, user)
        }

        findViewById<Button>(R.id.btn_takePhoto).setOnClickListener{
            //Launch camera to let user take picture
        }

        //queryPosts()

    }

    // Send a Post object to our Parse server
    fun submitPost(description: String, user: ParseUser){
        // Create the Post Object
        val post = Post()
        post.setDescription(description)
        post.setDescription(description)
        post.setUser(user)
        post.saveInBackground { exception ->
            if(exception != null){
                //Something has went wrong
                Log.e(TAG, "Error while saving post")
                exception.printStackTrace()
                Toast.makeText(this, "Error while saving post", Toast.LENGTH_SHORT).show()
            } else {
                Log.i(TAG, "Successfully saved post")
                // Reset the EditText field to be empty
                findViewById<EditText>(R.id.et_description).text.clear()
            }
        }

    }

    //Query for all posts in out server
    fun queryPosts(){
        // Specify which class to query
        val query: ParseQuery<Post> = ParseQuery.getQuery(Post::class.java)
        // Find all Post objects
        query.include(Post.KEY_USER)
        query.findInBackground(object: FindCallback<Post> {
            override fun done(posts: MutableList<Post>?, e: ParseException?) {
                if(e != null){
                    // Something went wrong
                    Log.e(TAG, "Error fetching posts")
                } else {
                    if (posts != null){
                        for(post in posts){
                            Log.i(TAG, "Post: " + post.getDescription() + ", username: " + post.getUser()?.username)
                        }
                    }
                }
            }

        })
    }

    companion object{
        const val TAG = "MainActivity"
    }
}