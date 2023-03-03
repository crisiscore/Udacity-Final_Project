package com.example.android.politicalpreparedness.utils

import com.example.android.politicalpreparedness.network.models.RepresentativeResponse
import com.example.android.politicalpreparedness.representative.model.Representative

fun RepresentativeResponse.mapResponseToRepresentativeList(): List<Representative> {
    val offices = this.offices
    val officials = this.officials
    return offices.flatMap { office -> office.getRepresentatives(officials) }
}