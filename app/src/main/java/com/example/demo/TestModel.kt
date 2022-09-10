package com.example.demo

class TestModel {
    data class  status(
        val code : Int,
        val message : String
    )

    data class list_img(
        val url: String
    )

    data class regularPrice(
        val value :String,
        val currency : String
    )

    data class  finalPrice(
        val value :String,
        val currency : String
    )

    data class discount(
        val amountOff : String,
        val percentOff : String
    )
    data class minimumPrice(
        val regularPrice : regularPrice,
        val finalPrice : finalPrice,
        val discount : discount
    )

    data class promotion_url(
        val image : String
    )

    data class Detail(
        val title : String,
        val content : String
    )

    data class Data(
        val id : Int,
        val sku : String,
        val name : String,
        val mediaGallery : Array<list_img>,
        val priceRange : minimumPrice,
        val promotions : Array<promotion_url>,
        val tabs : Array<Detail>

    )

    data class TestModel(
        val status: status,
        val data: Data
    )
}