package com.lesnyg.mychatapp.ui


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.lesnyg.mychatapp.ChatClient
import com.lesnyg.mychatapp.MsgInfo
import com.lesnyg.mychatapp.R
import com.lesnyg.mychatapp.databinding.ItemChatMeBinding
import com.lesnyg.mychatapp.databinding.ItemChatYouBinding
import kotlinx.android.synthetic.main.fragment_chat.*
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch

class ChatFragment : Fragment(), CoroutineScope by CoroutineScope(Dispatchers.IO) {
    val args: ChatFragmentArgs by navArgs()


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_chat, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = ChatAdapter(args.nickName)
        recycler_view.layoutManager = LinearLayoutManager(requireContext())
        recycler_view.adapter = adapter

        val client = ChatClient(args.nickName) { msgInfo ->
            launch(Dispatchers.Main) {
                adapter.items.add(msgInfo)
                adapter.notifyItemInserted(adapter.items.size -1)
            }
        }
        launch {
            client.connect()
        }

        send_btn.setOnClickListener {
            launch {
                client.writer.weite(chat_edit.text.toString())
                launch(Dispatchers.Main){
                    chat_edit.setText("")
                }
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
    }

}

class ChatAdapter(val nickName: String) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {


    companion object {
        const val ME_TYPE = 0
        const val YOU_TYPE = 1
    }

    var items = arrayListOf<MsgInfo>()

    interface MyHolder

    class ChatMeHolder(val binding: ItemChatMeBinding) : RecyclerView.ViewHolder(binding.root),MyHolder

    class ChatYouHolder(val binding: ItemChatYouBinding) : RecyclerView.ViewHolder(binding.root),MyHolder

    override fun getItemViewType(position: Int): Int {
        if (items[position].nickName == nickName) {
            return ME_TYPE
        }
        return YOU_TYPE
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ME_TYPE -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_chat_me, parent, false)
                val viewHolder = ChatMeHolder(ItemChatMeBinding.bind(view))
                viewHolder
            }
            else -> {
                val view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_chat_you, parent, false)
                val viewHolder = ChatYouHolder(ItemChatYouBinding.bind(view))
                viewHolder
            }
        }
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if(holder is ChatMeHolder){
            holder.binding.info = items[position]
        }else if(holder is ChatYouHolder){
            holder.binding.info = items[position]
        }
    }
}
