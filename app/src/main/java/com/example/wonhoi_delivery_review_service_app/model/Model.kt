package com.example.wonhoi_delivery_review_service_app.model

import android.annotation.SuppressLint
import androidx.recyclerview.widget.DiffUtil

// 데이터 변형을 쉽게 noti 하고 변형을 알아차려서 ui 의 변경도 자동으로 되도록 정의 예정
abstract class Model(
    open val id : Long,
    open val type : CellType
) {
        companion object {
                // 디태식이 오랜만 :)
                val DIFF_CALLBACK : DiffUtil.ItemCallback<Model> = object  : DiffUtil.ItemCallback<Model>() {
                        override fun areItemsTheSame(oldItem: Model, newItem: Model): Boolean {
                                return oldItem.id == newItem.id && oldItem.type == newItem.type
                        }

                        @SuppressLint("DiffUtilEquals")
                        override fun areContentsTheSame(oldItem: Model, newItem: Model): Boolean {
                                return oldItem === newItem
                        }
                }

        }
}