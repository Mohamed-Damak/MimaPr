package com.example.myapplication


import android.annotation.SuppressLint
import android.app.*
import android.content.ContentValues
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.findNavController
import kotlinx.android.synthetic.main.fragment_add_note.*
import java.util.*


open class addNoteFragment : Fragment() {
    @SuppressLint("UseRequireInsteadOfGet")
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return inflater.inflate(R.layout.fragment_add_note, container, false)
    }
    var id: Int? = null
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val etTitle = view.findViewById<EditText>(R.id.etTitle)
        val etDescription = view.findViewById<EditText>(R.id.etDescription)
        buAddNotes.setOnClickListener { addNotesevent() }
        setHasOptionsMenu(true)
        //check if Edit
        id = arguments?.getInt("ID")
        if (id != 0) {
            val title = arguments?.getString("Title")
            etTitle.setText(title)
            val description = arguments?.getString("Description")
            etDescription.setText(description)
        }
        subbtn.setOnClickListener { mettrenotification() }
        createNotificationChannel()
}
    @SuppressLint("UseRequireInsteadOfGet")
    fun mettrenotification(){
        val intent = Intent(context?.applicationContext, Notification::class.java)
        val title = TitreEt.text.toString()
        val message = MessageEt.text.toString()
        intent.putExtra(titleExtra, title)
        intent.putExtra(messageExtra, message)
        val pendingIntent= PendingIntent.getBroadcast(
            context?.applicationContext,
            notificationID,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )
        val alarmManager =this.activity!!.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val time = getTime()
/*        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            time,
            pendingIntent
        )*/
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis(), AlarmManager.INTERVAL_DAY * 7, pendingIntent);

        showAlert(time, title, message)
    }
    @SuppressLint("UseRequireInsteadOfGet")
    fun showAlert(time: Long, title: String, message: String)
    {
        val date = Date(time)
        val dateFormat = android.text.format.DateFormat.getLongDateFormat(context?.applicationContext)
        val timeFormat = android.text.format.DateFormat.getTimeFormat(context?.applicationContext)
        AlertDialog.Builder(this!!.activity!!)
            .setTitle("Notification ")
            .setMessage(
                "Titre: " + title +
                        "\nMessage: " + message +
                        "\nDans: " + dateFormat.format(date) + " " + timeFormat.format(date))
            .setPositiveButton("D'accord"){_,_ ->}
            .show()
    }
    fun getTime(): Long
    {
        val minute = timePicker.minute
        val hour = timePicker.hour
        val day =datePicker.dayOfMonth
        val month = datePicker.month
        val year = datePicker.year

        val calendar = Calendar.getInstance()
        calendar.set(year, month, day, hour, minute)
        return calendar.timeInMillis
}
    @SuppressLint("UseRequireInsteadOfGet")
    fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Notif Channel"
            val desc = "A Description of the Channel"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(channelID, name, importance)
            channel.description = desc
            val notification = activity!!.getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notification.createNotificationChannel(channel)
        }}
@SuppressLint("UseRequireInsteadOfGet")
    fun addNotesevent(){
        val etitle = view!!.findViewById<EditText>(R.id.etTitle)
        val title=etitle.text.toString()
        val etDescription=view!!.findViewById<EditText>(R.id.etDescription)
        val description=etDescription.text.toString()
        val values= ContentValues()
        values.put("Title",title)
        values.put("Description",description)
        val dbmanager=DbManager(this!!.activity!!)
        if(id!=0){
            val selectionArgs = arrayOf(id.toString())
            val id=dbmanager.update(values,"ID=?",selectionArgs)
            if (id>0){
                Toast.makeText(this!!.activity!!,"L'enregistrement est  modifié ",Toast.LENGTH_LONG).show()
            } else {
                Toast.makeText(this!!.activity!!,"L'enregistrement n'a pas  modifié", Toast.LENGTH_LONG).show()
            }
        }
        else{
            val id=dbmanager.insertNote(values)
            if (id>0){
                Toast.makeText(this!!.activity!!,"L'enregistrement est ajouté ",Toast.LENGTH_LONG).show()
            } else{
                Toast.makeText(this!!.activity!!,"Echec de l'ajout de l'enregistrement", Toast.LENGTH_LONG).show()
            }
        }}
    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.addnotes, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }
    @SuppressLint("UseRequireInsteadOfGet")
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.backbu -> {
                view!!.findNavController().navigate(R.id.noteListFragment)
            }
        }
        return super.onOptionsItemSelected(item)
    }
}