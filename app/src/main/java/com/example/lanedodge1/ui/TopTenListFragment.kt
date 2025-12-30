package com.example.lanedodge1.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.fragment.app.Fragment
import com.example.lanedodge1.R
import com.example.lanedodge1.logic.RecordManager
import com.example.lanedodge1.model.Record
import com.google.android.material.textview.MaterialTextView
import com.example.lanedodge1.interfaces.CallbackRecordClicked



class TopTenListFragment : Fragment() {

    private lateinit var topTen_LAYOUT_list: LinearLayoutCompat
    private lateinit var recordManager: RecordManager
    var callbackRecordClicked: CallbackRecordClicked? = null



    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {

        val v = inflater.inflate(
        R.layout.fragment_top_ten_list,
        container, false)

        findViews(v)

        recordManager = RecordManager(requireContext())
        val records: List<Record> = recordManager.getRecords()
        records.forEachIndexed { index, record ->
            val itemView = inflater.inflate(
                R.layout.item_top_ten,
                topTen_LAYOUT_list,
                false
            )

            val scoreTXT =
                itemView.findViewById<MaterialTextView>(R.id.record_TXT_score)
            val nameTXT =
                itemView.findViewById<MaterialTextView>(R.id.record_TXT_name)
            val locationTXT =
                itemView.findViewById<MaterialTextView>(R.id.record_TXT_location)

            val rank = index + 1

            scoreTXT.text = record.score.toString()
            locationTXT.text = "Lat: ${record.lat}, Lon: ${record.lon}"
            val rankTXT = itemView.findViewById<MaterialTextView>(R.id.record_TXT_rank)
            nameTXT.text = record.name
            rankTXT.text = rank.toString()

            topTen_LAYOUT_list.addView(itemView)

            itemView.setOnClickListener {
                callbackRecordClicked?.recordClicked(
                    record.lat,
                    record.lon
                )
            }
        }

        return v
    }


    private fun findViews(v: View) {
        topTen_LAYOUT_list = v.findViewById(R.id.topTen_LAYOUT_list)
    }
}
