package idn.project_whatsapp.whatsapp.fragments

import idn.project_whatsapp.whatsapp.R
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import idn.project_whatsapp.whatsapp.adapters.StatusListAdapter
import idn.project_whatsapp.whatsapp.listener.StatusItemClickListener
import idn.project_whatsapp.whatsapp.utils.Constants.DATA_USERS
import idn.project_whatsapp.whatsapp.utils.Constants.DATA_USER_CHATS
import idn.project_whatsapp.whatsapp.utils.StatusListElement
import idn.project_whatsapp.whatsapp.utils.User
import kotlinx.android.synthetic.main.activity_status_list_fragment.*

class StatusListFragment : Fragment(), StatusItemClickListener {

    private val firebaseDb = FirebaseFirestore.getInstance()
    private val userId = FirebaseAuth.getInstance().currentUser?.uid
    private var statusListAdapter = StatusListAdapter(arrayListOf())

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
// Inflate the layout for this fragment
        return inflater.inflate(R.layout.activity_status_list_fragment, container, false) }
    override fun onItemClicked(statusElement: StatusListElement) { Toast.makeText(context, userId, Toast.LENGTH_SHORT).show()
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        statusListAdapter.setOnItemClickListener(this)
        rv_status_list.apply {
            setHasFixedSize(false)
            layoutManager = LinearLayoutManager(context)
            adapter = statusListAdapter
            addItemDecoration(DividerItemDecoration(context, DividerItemDecoration.VERTICAL))
        }
        onVisible()
        fab_status_list.setOnClickListener { onVisible()
        }
    }
    fun onVisible() { statusListAdapter.onRefresh()
        refreshList()
    }
    fun refreshList() { firebaseDb.collection(DATA_USERS).document(userId!!).get()
        .addOnSuccessListener {
            if (it.contains(DATA_USER_CHATS)) { // mengakses userChats dari User
                val partners = it[DATA_USER_CHATS] // mendapatkan id partner
                for (partner in (partners as HashMap<String, String>).keys) {
                firebaseDb.collection(DATA_USERS).document(partner).get()
                    .addOnSuccessListener { documentSnapshot ->
                    val partner = documentSnapshot.toObject(User::class.java)
                        if (partner != null) {
                            if (!partner.status.isNullOrEmpty() || !partner.statusUrl.isNullOrEmpty()) {
                                val newElement = StatusListElement(
                                    partner.name,
                                    partner.imageUrl,
                                    partner.status,
                                    partner.statusUrl,
                                    partner.statusTime
                    )
                        statusListAdapter.addElement(newElement)
                    } }
                }
            } }
    }
} }
