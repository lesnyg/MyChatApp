package com.lesnyg.mychatapp.ui


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.lesnyg.mychatapp.R
import kotlinx.android.synthetic.main.fragment_lobby.*

class LobbyFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_lobby, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        enter_btn.setOnClickListener {
            val nickName = chatId.text.toString()
           if (nickName.isNotEmpty())  {
               val action = LobbyFragmentDirections
                   .actionLobbyFragmentToChatFragment(chatId.text.toString())

               findNavController().navigate(action)

           }
        }
    }

}
