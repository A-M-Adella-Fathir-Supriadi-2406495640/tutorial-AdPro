# Exercise 1

## Reflection – Clean Code and Secure Coding Practices

Pada proyek ini, saya telah mengimplementasikan dua fitur baru menggunakan Spring Boot, yaitu fitur Edit Product dan Delete Product. Setelah mengimplementasikan fitur tersebut, saya melakukan evaluasi terhadap source code berdasarkan prinsip clean code dan secure coding yang telah dipelajari pada modul ini.

---

## CLEAN CODE PRINCIPLES

### Separation of Concerns
Aplikasi ini menggunakan arsitektur berlapis yang jelas. Controller hanya menangani request dan response, Service berisi logika bisnis, Repository bertanggung jawab pada pengelolaan data, dan Model merepresentasikan entitas Product. Pemisahan ini membuat kode lebih mudah dibaca, dipahami, dan dirawat.

### Penamaan Class dan Method yang Jelas
Nama class seperti ProductController, ProductService, dan ProductRepository sudah menggambarkan tanggung jawab masing-masing. Method seperti create, findAll, findById, update, dan deleteById juga jelas menunjukkan fungsinya.

### Struktur Kode yang Konsisten
Setiap fitur (create, edit, delete) mengikuti alur yang konsisten dari Controller ke Service lalu ke Repository. Hal ini memudahkan pengembangan fitur baru dan debugging.

### Penghindaran Duplikasi Kode
Logika bisnis tidak ditulis langsung di Controller, tetapi didelegasikan ke Service. Hal ini menghindari duplikasi kode dan membuat Controller tetap sederhana.

---

## SECURE CODING PRACTICES

### Penggunaan HTTP Method yang Tepat
Fitur delete menggunakan metode POST, bukan GET, sehingga mengurangi risiko penghapusan data secara tidak sengaja melalui URL.

### Pembatasan Akses Data Melalui Service Layer
Controller tidak berinteraksi langsung dengan Repository. Semua akses data dilakukan melalui Service, sehingga logika bisnis dan validasi dapat dikontrol dengan lebih baik.

### Validasi Alur Data
Pada fitur edit, data produk diambil berdasarkan productId. Jika data tidak ditemukan, aplikasi melakukan redirect ke halaman list sehingga menghindari error seperti NullPointerException.

### Penggunaan Redirect Setelah Operasi Data
Setelah operasi create, update, dan delete, aplikasi melakukan redirect ke halaman list. Hal ini mencegah duplicate submission ketika halaman di-refresh.

---

## EVALUASI DAN PERBAIKAN KODE

### Penyimpanan Data Masih In-Memory
Saat ini data produk disimpan menggunakan List di Repository. Hal ini tidak aman untuk aplikasi produksi karena data akan hilang saat aplikasi dimatikan. Perbaikan yang dapat dilakukan adalah menggunakan database dan Spring Data JPA.

### Belum Ada Validasi Input
Aplikasi belum menerapkan validasi input seperti memastikan productName tidak kosong dan productQuantity tidak bernilai negatif. Perbaikan dapat dilakukan dengan menambahkan anotasi validasi seperti @NotBlank dan @Min.

### Tidak Ada Error Handling Khusus
Saat productId tidak ditemukan, aplikasi hanya melakukan redirect tanpa memberikan pesan kesalahan. Perbaikan dapat dilakukan dengan menambahkan handling error atau pesan feedback ke pengguna.

### Keamanan Masih Terbatas
Aplikasi belum menggunakan autentikasi dan otorisasi. Untuk meningkatkan keamanan, dapat ditambahkan Spring Security agar hanya pengguna tertentu yang dapat melakukan edit dan delete.

---

## KESIMPULAN

Secara keseluruhan, kode yang dibuat sudah menerapkan prinsip clean code dengan struktur yang jelas dan alur yang konsisten. Namun, masih terdapat beberapa aspek yang dapat ditingkatkan terutama pada validasi, error handling, dan keamanan agar aplikasi lebih siap digunakan pada lingkungan produksi.

---

# Exercise 2

## Refleksi setelah menulis Unit Test

Setelah menulis unit test, saya merasa lebih yakin terhadap perilaku kode yang saya buat karena setiap fungsi diuji secara terisolasi. Unit test membantu saya memahami edge case dan memastikan perubahan kode tidak merusak fitur yang sudah ada. Jumlah unit test dalam satu kelas tidak memiliki angka pasti, tetapi idealnya setiap method publik dan setiap kemungkinan kondisi penting (kasus normal, batas, dan error) memiliki test masing-masing. Untuk memastikan unit test sudah cukup, kita dapat menggunakan metrik seperti code coverage untuk melihat seberapa besar bagian kode yang dieksekusi oleh test. Namun, memiliki 100% code coverage tidak berarti kode bebas dari bug atau error, karena test bisa saja tidak memverifikasi logika dengan benar, tidak menguji skenario nyata, atau melewatkan bug yang bersifat konseptual dan integrasi antar komponen.

---

## Refleksi kebersihan kode pada Functional Test tambahan

Jika saya membuat functional test suite baru untuk memverifikasi jumlah item dalam product list dengan struktur dan setup yang sama seperti functional test sebelumnya, maka dari sisi kebersihan kode akan muncul beberapa masalah. Salah satu isu utama adalah duplikasi kode, seperti setup WebDriver, baseUrl, dan konfigurasi anotasi yang sama di banyak kelas test. Duplikasi ini menurunkan maintainability karena jika ada perubahan konfigurasi, maka semua kelas test harus diubah satu per satu. Selain itu, test menjadi lebih sulit dibaca karena banyak boilerplate code yang tidak langsung berkaitan dengan tujuan pengujian. Untuk memperbaikinya, setup umum dapat diekstrak ke superclass abstrak atau utility class khusus untuk functional test. Dengan begitu, setiap test suite hanya fokus pada skenario pengujian masing-masing, kode menjadi lebih bersih, lebih mudah dirawat, dan kualitas keseluruhan test code meningkat.

---

## Kesimpulan

Unit test dan functional test memiliki peran yang berbeda namun saling melengkapi. Unit test membantu memastikan logika internal berjalan dengan benar, sedangkan functional test memastikan sistem bekerja sesuai perspektif pengguna. Penerapan clean code dalam test code sama pentingnya dengan production code agar proyek tetap terstruktur dan maintainable dalam jangka panjang.





# Tutorial 2

## REFLEKSI CI/CD DAN CODE QUALITY

Code Quality Issue yang Diperbaiki
Selama pengerjaan exercise, saya menemukan satu pelanggaran aturan dari PMD pada file ProductController.java.
Issue yang muncul adalah:
AvoidDuplicateLiterals — String literal "redirect:/product/list" muncul sebanyak 4 kali dalam satu file.
Masalah ini terjadi karena saya menuliskan string yang sama berulang-ulang di beberapa method. Praktik ini tidak disarankan karena:
- Mengurangi maintainability
- Berisiko menimbulkan inkonsistensi jika suatu saat perlu diubah
- Melanggar prinsip clean code

Strategi perbaikan yang saya lakukan:
Saya membuat sebuah constant dengan modifier private static final, misalnya:

private static final String REDIRECT_PRODUCT_LIST = "redirect:/product/list";
Kemudian seluruh penggunaan literal tersebut saya ganti menjadi:

return REDIRECT_PRODUCT_LIST;

Dengan cara ini:
- Duplikasi literal hilang
- Kode menjadi lebih rapi
- Jika ingin mengubah URL redirect, cukup ubah di satu tempat
- PMD tidak lagi mendeteksi pelanggaran tersebut pada workflow berikutnya

Setiap perbaikan saya commit secara terpisah agar riwayat perubahan tetap jelas dan terstruktur.

## Apakah Implementasi Ini Sudah Memenuhi Continuous Integration dan Continuous Deployment?

Menurut saya, implementasi yang saya buat sudah memenuhi konsep Continuous Integration dan Continuous Deployment.
Pertama, setiap kali ada push ke repository, GitHub Actions secara otomatis menjalankan workflow yang berisi proses build, unit test, dan code analysis (PMD). Hal ini sesuai dengan prinsip Continuous Integration karena setiap perubahan kode langsung diuji dan diverifikasi secara otomatis.
Kedua, code scanning dan quality analysis juga dijalankan secara otomatis. Jika ditemukan pelanggaran aturan atau test gagal, maka workflow akan gagal. Ini memastikan bahwa hanya kode yang memenuhi standar kualitas yang bisa lanjut ke tahap berikutnya.
Ketiga, setelah branch module-2-exercise digabungkan ke main, sistem otomatis melakukan deployment ke PaaS. Deployment terjadi tanpa intervensi manual setiap kali ada perubahan pada branch utama. Hal ini sudah mencerminkan Continuous Deployment karena setiap perubahan yang lolos pipeline langsung dipublikasikan ke environment yang dapat diakses.
Dengan adanya proses otomatis untuk build, test, code analysis, dan deployment, maka pipeline yang saya implementasikan sudah memenuhi definisi CI/CD secara end-to-end.

---

# Exercise 3

## 1) Explain what principles you apply to your project!

- SRP (Single Responsibility Principle): Memisahkan CarController dari ProductController. Sekarang, CarController hanya bertanggung jawab atas alur logika mobil, bukan produk secara umum.
- OCP (Open-Closed Principle): Menggunakan interface CarService. Jika di masa depan ingin menambah cara penyimpanan (misal: CarDatabaseService),cukup menambah kelas baru tanpa mengubah kode di CarController.
- LSP (Liskov Substitution Principle): Dengan menghapus inheritance yang tidak tepat (CarController extends ProductController), kita menghindari situasi di mana CarController dipaksa memiliki perilaku Product yang mungkin tidak relevan atau merusak konsistensi program.
- ISP (Interface Segregation Principle): Memastikan CarService hanya berisi metode yang dibutuhkan oleh objek mobil (seperti deleteCarById), sehingga implementasinya tidak terbebani oleh metode dari objek lain.
- DIP (Dependency Inversion Principle): Di dalam CarController, kita menggunakan @Autowired private CarService carService; (tergantung pada abstraksi/interface), bukan langsung ke CarServiceImpl (kelas konkret).

## 2) Explain the advantages of applying SOLID principles to your project with examples
Penerapan SOLID memberikan keuntungan jangka panjang bagi pemeliharaan kode:

- Kemudahan Pengujian (Maintainability): Karena setiap kelas hanya punya satu tugas (SRP), kita bisa membuat unit test yang sangat spesifik tanpa takut terganggu oleh logika lain.
Contoh: Saat kita mengetes CarRepository, kita yakin tes tersebut hanya mengetes penyimpanan mobil, bukan validasi produk.

- Fleksibilitas (Flexibility): Dengan DIP, kita bisa mengganti implementasi tanpa menyentuh kontroler.
Contoh: Jika kamu ingin mengganti penyimpanan dari ArrayList ke Database, kamu hanya perlu mengubah implementasi di Service tanpa mengubah satu baris pun di CarController.

- Kode Lebih Bersih (Readability): Memisahkan tanggung jawab membuat kode lebih mudah dibaca oleh rekan tim.

## 3) Explain the disadvantages of not applying SOLID principles to your project with examples.
- Rigidity (Kekakuan): Kode sulit diubah karena satu perubahan kecil merembet ke tempat lain.
Contoh: Jika CarController masih extends ProductController, setiap kali kita mengubah cara kerja URL di ProductController, CarController bisa ikut rusak secara tidak sengaja.

- Code Duplication & TODO Stubs: Tanpa implementasi yang benar (SRP), kita akan berakhir dengan banyak kode "TODO" yang tidak pernah selesai di Service Implementation, membuat aplikasi tidak fungsional.

- Sulit Melakukan Testing: Karena kelas-kelas saling terikat erat (tightly coupled), kita sulit melakukan isolasi saat terjadi bug.
Contoh: Kamu sulit mencari tahu apakah error berasal dari logika Car atau logika Product yang diwarisinya.
---

# Reflection 4

## 1. Reflect based on Percival (2017) proposed self-reflective questions (in “Principles and Best
Practice of Testing” submodule, chapter “Evaluating Your Testing Objectives”), whether this
TDD flow is useful enough for you or not. If not, explain things that you need to do next time
you make more tests.

Berdasarkan pertanyaan refleksi diri yang diusulkan oleh Percival (2017), saya merasa alur TDD yang digunakan dalam latihan ini sangat bermanfaat, terutama dalam memberikan panduan yang jelas selama pengembangan. Dengan menulis tes terlebih dahulu (fase Red), saya dipaksa untuk memahami persyaratan dan edge cases (seperti status yang tidak valid atau daftar produk kosong) sebelum menulis kode produksi. Hal ini mencegah adanya kode yang tidak perlu dan memastikan setiap baris kode memiliki tujuan yang spesifik.
Namun, untuk meningkatkan kualitas pengujian saya berikutnya, saya perlu lebih fokus pada:
- Menguji alasan ("Why") bukan hanya "Apa": Memastikan tes menjelaskan niat logika bisnis, bukan sekadar mengecek apakah sebuah variabel telah terisi.
- Mengurangi ketergantungan (Coupling): Dalam beberapa kasus, tes sangat terikat dengan implementasi internal. Saya harus berusaha menulis tes yang lebih tahan banting terhadap perubahan struktur kode (refactoring).

## 2. You have created unit tests in Tutorial. Now reflect whether your tests have successfully
followed F.I.R.S.T. principle or not. If not, explain things that you need to do the next time you
create more tests.

Setelah membuat unit test untuk Order, OrderRepository, dan OrderService, saya telah mengevaluasinya berdasarkan prinsip F.I.R.S.T.:
- Fast (Cepat): Ya. Tes berjalan cepat karena kita menggunakan Mockito untuk melakukan mock pada lapisan repository di dalam tes service, sehingga menghindari operasi I/O yang lambat.
- Independent (Mandiri): Ya. Setiap kasus tes (Happy/Unhappy) di OrderRepositoryTest dan OrderServiceImplTest dapat berjalan sendiri. Penggunaan @BeforeEach memastikan data diatur ulang sebelum setiap tes dijalankan.
- Repeatable (Dapat Diulang): Ya. Tes tidak bergantung pada faktor eksternal seperti database asli atau koneksi internet, sehingga hasilnya selalu sama di lingkungan mana pun.
- Self-Validating (Validasi Otomatis): Ya. Setiap tes menggunakan assertion JUnit yang jelas (assertEquals, assertThrows, assertNull) untuk menentukan lulus atau gagal tanpa perlu pengecekan manual.
- Timely (Tepat Waktu): Ya. Kita mengikuti alur kerja TDD di mana tes ditulis sebelum implementasi sebenarnya dilakukan (Red-Green-Refactor).

Meskipun tes sudah mengikuti prinsip dengan baik, saya dapat meningkatkan aspek Self-Validating menjadi lebih informatif dengan menambahkan pesan deskriptif pada assertion untuk mempercepat proses debugging ketika terjadi kegagalan tes di masa mendatang.




















