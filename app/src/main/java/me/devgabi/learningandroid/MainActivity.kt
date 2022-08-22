package me.devgabi.learningandroid

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.*
import com.google.android.material.textfield.TextInputEditText

class MainActivity : AppCompatActivity() {
    private val todoItems = (1..10)
        .map { Todo("Item $it") }
        .toMutableList()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<RecyclerView>(R.id.todo_list)
            .apply {
                setHasFixedSize(true)
                addItemDecoration(DividerItemDecoration(this@MainActivity, DividerItemDecoration.VERTICAL))

                layoutManager = LinearLayoutManager(this@MainActivity)
                adapter = TodoListAdapter(todoItems)
            }
            .also { todoRecyclerView ->
                findViewById<Button>(R.id.add_todo).setOnClickListener {
                    val textInputEditText = findViewById<TextInputEditText>(R.id.write_todo_input)
                    val content = textInputEditText.text.toString()

                    if (content.isBlank())
                        return@setOnClickListener

                    if (todoItems.find { it.content.equals(content, ignoreCase = true) } != null)
                        return@setOnClickListener

                    todoItems.add(Todo(content))

                    textInputEditText.text?.clear()

                    todoRecyclerView.adapter!!.notifyDataSetChanged()
                }
            }
    }

    data class Todo(val content: String) {
        object TodoDiffCallback : DiffUtil.ItemCallback<Todo>() {
            override fun areItemsTheSame(oldItem: Todo, newItem: Todo) = oldItem == newItem
            override fun areContentsTheSame(oldItem: Todo, newItem: Todo) = oldItem == newItem
        }
    }

    class TodoListAdapter(private val content: MutableList<Todo>) :
        ListAdapter<Todo, TodoListAdapter.Holder>(Todo.TodoDiffCallback) {

        class Holder(view: View) : RecyclerView.ViewHolder(view) {
            val content: TextView = view.findViewById(R.id.text)
            val button: Button = view.findViewById(R.id.button)
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
            val view = LayoutInflater
                .from(parent.context)
                .inflate(R.layout.todo_list_item, parent, false) as View

            return Holder(view)
        }

        override fun getItemCount() = content.size

        override fun onBindViewHolder(holder: Holder, position: Int) {
            holder.content.text = content[position].content
            holder.button.setOnClickListener {
                content.removeAt(position)

                notifyDataSetChanged()
            }
        }
    }
}