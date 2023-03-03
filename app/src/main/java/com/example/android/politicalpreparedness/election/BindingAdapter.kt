package com.example.android.politicalpreparedness.election

import android.widget.Button
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.android.politicalpreparedness.R
import com.example.android.politicalpreparedness.network.models.VoterInfoResponse

@BindingAdapter("electionInfoTitle")
fun bindElectionInfoTitle(view: TextView, voterInfo: VoterInfoResponse?) {
    voterInfo?.run {
        view.text = view.resources.getString(R.string.election_info_label, voterInfo.state?.firstOrNull()?.name)
    }
}

@BindingAdapter("followButtonText")
fun bindFollowButton(button: Button, isElectionSaved: Boolean?) {
    if (isElectionSaved != null) {
        if (isElectionSaved) {
            button.text = button.resources.getString(R.string.unfollow_text)
        } else {
            button.text = button.resources.getString(R.string.follow_text)
        }
    } else {
        button.text = ""
    }
}