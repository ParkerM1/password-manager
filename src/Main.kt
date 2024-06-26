// Besides the last one all the setup is for the encryption
import java.security.Key
import javax.crypto.Cipher
import javax.crypto.SecretKey
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec
import javax.crypto.spec.SecretKeySpec
import java.security.SecureRandom
import java.util.Base64
import java.io.File

// putting the code in a class
class PasswordManager(private val masterPassword: String) {
    private val passwords: MutableMap<String, String> = mutableMapOf()

    init {
        loadPasswords()
    }
    // function: takes the name of the service and the password and saves it
    fun addPassword(service: String, password: String) {
        passwords[service] = password
        savePasswords()
    }
    // function: gets the name of the service and gives you the password
    fun getPassword(service: String): String? {
        val encryptedPassword = passwords[service]
        return if (encryptedPassword != null) (encryptedPassword) else null
    }
    // function: tries to get the password from password.txt and displays it
    private fun loadPasswords() {
        try {
            val file = File("passwords.txt")
            if (file.exists()) {
                val lines = file.readLines()
                for (line in lines) {
                    val (service, encryptedPassword) = line.split(":")
                    passwords[service] = encryptedPassword
                }
            }
        } catch (e: Exception) {
            println("Error loading passwords: ${e.message}")
        }
    }
    // function: saves the password and service on the password.txt
    private fun savePasswords() {
        try {
            val file = File("passwords.txt")
            file.printWriter().use { out ->
                for ((service, encryptedPassword) in passwords) {
                    out.println("$service:$encryptedPassword")
                }
            }
        } catch (e: Exception) {
            println("Error saving passwords: ${e.message}")
        }
    }
// all the code that is commited is code that I have been working on to encrypt the password
    // I could not get it to function correctly

//    private fun encrypt(text: String): String {
//        val keySpec = generateKeySpec(masterPassword)
//        val secretKey = SecretKeySpec(keySpec.encoded, "AES")
//        val cipher = Cipher.getInstance("AES")
//        cipher.init(Cipher.ENCRYPT_MODE, secretKey)
//        val encryptedBytes = cipher.doFinal(text.toByteArray())
//        return Base64.getEncoder().encodeToString(encryptedBytes)
//    }
//
//    private fun decrypt(text: String): String {
//        val keySpec = generateKeySpec(masterPassword)
//        val secretKey = SecretKeySpec(keySpec.encoded, "AES")
//        val cipher = Cipher.getInstance("AES")
//        cipher.init(Cipher.DECRYPT_MODE, secretKey)
//        val decryptedBytes = cipher.doFinal(Base64.getDecoder().decode(text))
//        return String(decryptedBytes)
//    }
//
//    private fun generateKeySpec(password: String): SecretKey {
//        val salt = ByteArray(16)
//        val random = SecureRandom()
//        random.nextBytes(salt)
//        val factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256")
//        val spec = PBEKeySpec(password.toCharArray(), salt, 65536, 256)
//        return SecretKeySpec(factory.generateSecret(spec).encoded, "AES")
//    }
}

fun main() {
    println("Welcome to Password Manager!")
    print("Enter your master password: ")
    // the value takes the password
    val masterPassword = readLine() ?: ""

    val passwordManager = PasswordManager(masterPassword)
    // the while loop tests to see if the password is correct and take the user into the code if it is
    while (true) {
        println("\nMenu:")
        println("1. Add a new password")
        println("2. Get a password")
        println("3. Exit")
        print("Enter your choice: ")
        when (val choice = readLine()?.toIntOrNull()) {
            1 -> {
                print("Enter service name: ")
                val service = readLine() ?: ""
                print("Enter password: ")
                val password = readLine() ?: ""
                // uses addPassword function
                passwordManager.addPassword(service, password)
                println("Password added successfully for $service")
            }
            2 -> {
                print("Enter service name: ")
                val service = readLine() ?: ""
                // uses getPassword function
                val password = passwordManager.getPassword(service)
                if (password != null) {
                    println("Password for $service: $password")
                } else {
                    println("No password found for $service")
                }
            }
            3 -> {
                println("Exiting...")
                break
            }
            else -> println("Invalid choice")
        }
    }
}