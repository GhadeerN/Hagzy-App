package sa.edu.tuwaiq.hagzy.view

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.Glide
import sa.edu.tuwaiq.hagzy.databinding.DialogLayoutBinding
import sa.edu.tuwaiq.hagzy.util.DateFormat
import sa.edu.tuwaiq.hagzy.util.ShareImageUtil

class DetailsDialogFragment(val imgUrl: String, val dateUpload: String): DialogFragment() {

    private lateinit var binding: DialogLayoutBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DialogLayoutBinding.inflate(inflater, container, false)
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        Glide.with(view).load(imgUrl).into(binding.detailImageView)

        // Format the date and set its value in the textView
        val formattedDate = DateFormat().dateFormatted(dateUpload)
        binding.uploadDateTextView.text = formattedDate

        // Close the dialog
        binding.closeImageButton.setOnClickListener {
            dismiss()
        }

        // Share button
        binding.detailsShareImageButton.setOnClickListener {
            ShareImageUtil.shareImage(binding.detailImageView, requireActivity())
        }
    }
}