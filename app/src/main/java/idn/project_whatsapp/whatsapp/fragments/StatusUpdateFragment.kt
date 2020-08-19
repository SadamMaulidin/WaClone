package idn.project_whatsapp.whatsapp.fragments

import android.content.Intent
import idn.project_whatsapp.whatsapp.R
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import idn.project_whatsapp.whatsapp.utils.Constants.DATA_USERS
import idn.project_whatsapp.whatsapp.utils.Constants.DATA_USER_STATUS
import idn.project_whatsapp.whatsapp.utils.Constants.DATA_USER_STATUS_TIME
import idn.project_whatsapp.whatsapp.utils.Constants.DATA_USER_STATUS_URL
import idn.project_whatsapp.whatsapp.utils.Constants.REQUEST_CODE_PHOTO
import idn.project_whatsapp.whatsapp.utils.getTime
import idn.project_whatsapp.whatsapp.utils.populateImage
import kotlinx.android.synthetic.main.activity_profile.*
import kotlinx.android.synthetic.main.activity_profile.progress_layout
import kotlinx.android.synthetic.main.activity_status_update_fragment.*

class StatusUpdateFragment : Fragment() {

    private val firebaseDB = FirebaseFirestore.getInstance()
    private val firebaseStorage = FirebaseStorage.getInstance().reference
    private val userId = FirebaseAuth.getInstance().currentUser?.uid
    private var imageUrl = ""

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.activity_status_update_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        progress_layout.setOnTouchListener { v, event -> true }
        fab_status.setOnClickListener { onUpdate() }
        populateImage(context, imageUrl, img_status_update)

        lay_status.setOnClickListener {
            if (isAdded) {
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                startActivityForResult(intent, REQUEST_CODE_PHOTO)
            }
        }
    }

    fun onUpdate() { // ketika fab diklik akan memperbarui data-data di Firebase
        progress_layout.visibility = View.VISIBLE // sesuai acuan yang digunakan
        val map = HashMap<String, Any>()
        map[DATA_USER_STATUS] = edt_status_update.text.toString()
        map[DATA_USER_STATUS_URL] = imageUrl
        map[DATA_USER_STATUS_TIME] = getTime()
        firebaseDB.collection(DATA_USERS)
            .document(userId!!)
            .update(map)
            .addOnSuccessListener {
                progress_layout.visibility = View.GONE
                Toast.makeText(activity, "Status updated.", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                progress_layout.visibility = View.GONE
                Toast.makeText(activity, "Status update failed.", Toast.LENGTH_SHORT).show()
            }
    }
}

