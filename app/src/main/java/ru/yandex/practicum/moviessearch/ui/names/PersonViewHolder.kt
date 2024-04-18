package ru.yandex.practicum.moviessearch.ui.names

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import ru.yandex.practicum.moviessearch.R
import ru.yandex.practicum.moviessearch.domain.models.Person

class PersonViewHolder(parent: ViewGroup) :
    RecyclerView.ViewHolder(
        LayoutInflater.from(parent.context)
            .inflate(R.layout.list_item_person, parent, false)
    ) {

    var photo = itemView.findViewById(R.id.photo)
    var name = itemView.findViewById(R.id.name)
    var description = itemView.findViewById(R.id.description)

    fun bind(person: Person) {
        Glide.with(itemView)
            .load(person.photoUrl)
            .placeholder(R.drawable.ic_launcher_foreground)
            .circleCrop()
            .into(photo)
        name.text = person.name
        description.text = person.description
    }


}
