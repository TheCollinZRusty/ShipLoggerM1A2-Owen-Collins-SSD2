package org.wit.shiploggerm1a2.activities

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_shiplogger.*
import org.jetbrains.anko.AnkoLogger
import org.jetbrains.anko.info
import android.support.v7.widget.SearchView
import org.jetbrains.anko.toast
import org.wit.shiploggerm1a2.R
import org.wit.shiploggerm1a2.helpers.UsersDBHelper
import org.wit.shiploggerm1a2.helpers.readImage
import org.wit.shiploggerm1a2.helpers.readImageFromPath
import org.wit.shiploggerm1a2.helpers.showImagePicker
import org.wit.shiploggerm1a2.main.MainApp
import org.wit.shiploggerm1a2.models.ShipLoggerModel

class ShipLoggerActivity : AppCompatActivity(), AnkoLogger {

        var shiplogger = ShipLoggerModel()
        lateinit var app: MainApp
        var edit = true
        val IMAGE_REQUEST = 1
        lateinit var usersDBHelper : UsersDBHelper


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_shiplogger)






        usersDBHelper = UsersDBHelper(this)
        app = application as MainApp
        edit = true

        if (intent.hasExtra("ship_edit")) {
            shiplogger = intent.extras.getParcelable<ShipLoggerModel>("ship_edit")
            shipTitle.setText(shiplogger.title)
            shipDescription.setText(shiplogger.description)
            btnAdd.setText(R.string.save_ship)
            shipImage.setImageBitmap(readImageFromPath(this, shiplogger.image))
            if (shiplogger.image != null) {
            chooseImage.setText(R.string.change_ship_image)
            edit = true
            }
           }
        //The Adding Ships Button Behaviour
            btnAdd.setOnClickListener() {
            shiplogger.title = shipTitle.text.toString()
            shiplogger.description = shipDescription.text.toString()
            if (shiplogger.title.isEmpty()) {
            app.shiploggers.create(shiplogger.copy())
            toast(R.string.enter_ship_title)
            }
            else
            {
            if (edit)
            {
            app.shiploggers.update(shiplogger.copy())
            }
            else {
            app.shiploggers.create(shiplogger.copy())
            }
            }
            info("add Button Pressed: $shipTitle")
            setResult(AppCompatActivity.RESULT_OK)
            finish()
            }
            chooseImage.setOnClickListener {
            showImagePicker(this, IMAGE_REQUEST)
            }
            btnStartAnotherActivity.setOnClickListener {
            val intent = Intent(this, CameraActivity::class.java)
            // start your next activity
            startActivity(intent)
            }
            toolbarAdd.title = title
            setSupportActionBar(toolbarAdd)
            }

    //Inflates the Menu
    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_shiplogger, menu)
        //SearchView
        if (edit && menu != null) menu.getItem(0).setVisible(true)
            val searchItem = menu?.findItem(R.id.menu_search)
            if (searchItem != null) {
            val searchView = searchItem.actionView as SearchView
            searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
            return true
            }
            override fun onQueryTextChange(p0: String?): Boolean {
            return true
            }
            }
            )
            }
            return super.onCreateOptionsMenu(menu)
            }

    //Menu Items for Delete and Cancel
    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when (item?.itemId) {
            R.id.item_delete -> {
            app.shiploggers.delete(shiplogger)
            finish()
            }
            R.id.item_cancel -> {
            finish()
            }
            }
            return super.onOptionsItemSelected(item)
            }

    //Builds the activity
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)
            when (requestCode) {
            IMAGE_REQUEST -> {
            if (data != null) {
            shiplogger.image = data.getData().toString()
            shipImage.setImageBitmap(readImage(this, resultCode, data))
            chooseImage.setText(R.string.change_ship_image)
            }
            }
            }
            }
            }








