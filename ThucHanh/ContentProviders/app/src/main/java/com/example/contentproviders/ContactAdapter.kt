import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import com.example.contentproviders.R

class ContactAdapter(private val context: Context, private val contacts: List<String>) : BaseAdapter() {

    override fun getCount(): Int = contacts.size

    override fun getItem(position: Int): String = contacts[position]

    override fun getItemId(position: Int): Long = position.toLong()

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view: View = convertView ?: LayoutInflater.from(context).inflate(R.layout.list_item_contact, parent, false)

        val contactName = view.findViewById<TextView>(R.id.contactName)
        val contactImage = view.findViewById<ImageView>(R.id.contactImage)

        contactName.text = contacts[position]

        // Bạn có thể thay đổi ảnh đại diện nếu lấy được từ danh bạ
        contactImage.setImageResource(R.drawable.nguyn_vn_a)

        return view
    }
}
