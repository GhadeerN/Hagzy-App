package sa.edu.tuwaiq.hagzy.util

import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.time.LocalDateTime

class DateFormatTest{

    private lateinit var dateFormat:DateFormat


    @Before  //@Before test
    //so before every test the function will execute
    fun setup(){
        dateFormat = DateFormat()
    }

    @Test
    fun dateFormattedWithInvalidDateThenReturnFalseValue(){

        // "95094.545.45" -->> false data
        val validation = dateFormat.dateFormatted("1409193883")
        // here I except false and i pass the validation
        Assert.assertEquals(String, validation)
    }


}