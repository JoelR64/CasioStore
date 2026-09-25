package com.example.casiostore;

import android.content.Context;
import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

import java.util.concurrent.Executors;

@Database(entities = {
        ProductoEntity.class,
        CarritoEntity.class,
        UsuarioEntity.class,
        HistorialEntity.class,
        UltimaCompraEntity.class // <-- 1. Añadido aquí
}, version = 3, exportSchema = false) // <-- 2. Cambiado de version = 2 a version = 3
public abstract class CasioDatabase extends RoomDatabase {

    public abstract ProductoDao productoDao();
    public abstract CarritoDao carritoDao();
    public abstract UsuarioDao usuarioDao();
    public abstract HistorialDao historialDao();
    public abstract UltimaCompraDao ultimaCompraDao();

    private static volatile CasioDatabase INSTANCE;

    public static CasioDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (CasioDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    CasioDatabase.class, "casio_store_db")
                            .fallbackToDestructiveMigration()
                            .addCallback(sRoomDatabaseCallback)
                            .build();
                }
            }
        }
        return INSTANCE;
    }

    private static final RoomDatabase.Callback sRoomDatabaseCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            Executors.newSingleThreadExecutor().execute(() -> {
                CasioDatabase database = INSTANCE;
                if (database != null) {
                    ProductoDao pDao = database.productoDao();
                    UsuarioDao uDao = database.usuarioDao();

                    // --- 1. POBLAR PRODUCTOS ---
                    pDao.insertar(new ProductoEntity("Reloj Casio A-168WA", 1234.0, "Clásico digital retro con iluminador electro-luminiscente.", R.drawable.a168, "RELOJES", 1));
                    pDao.insertar(new ProductoEntity("Reloj Casio G-Shock GA-2100-1A1", 3500.0, "Resistente a impactos y sumergible para deportes extremos.", R.drawable.ga_2100_1a1, "RELOJES", 1));
                    pDao.insertar(new ProductoEntity("Reloj Casio Vintage LA670-7", 980.0, "Diseño elegante minimalista dorado con correa de acero.", R.drawable.la670wa_7, "RELOJES", 1));
                    pDao.insertar(new ProductoEntity("Reloj Casio Edifice EFR-S567D-1AV", 4500.0, "Cronógrafo deportivo de alta precisión en acero inoxidable.", R.drawable.efr_s567_1, "RELOJES", 1));
                    pDao.insertar(new ProductoEntity("Calculadora Científica FX-991ES", 350.0, "Calculadora científica con funciones avanzadas y pantalla natural.", R.drawable.fx_991esplus, "CALCULADORAS", 1));
                    pDao.insertar(new ProductoEntity("Calculadora Financiera FC-200V", 650.0, "Ideal para cálculos financieros, amortizaciones y estadísticas.", R.drawable.fc_200v, "CALCULADORAS", 1));
                    pDao.insertar(new ProductoEntity("Calculadora de Escritorio MS-80B", 120.0, "Pantalla grande de 8 dígitos con doble alimentación solar y pila.", R.drawable.ms_80b, "CALCULADORAS", 1));
                    pDao.insertar(new ProductoEntity("Calculadora Gráfica FX-CG50", 1800.0, "Pantalla a color de alta resolución y gráficos tridimensionales.", R.drawable.fx_cg50, "CALCULADORAS", 1));
                    pDao.insertar(new ProductoEntity("Piano Casio AP-550BK", 2000.0, "Piano digital Privia con acción de martillo y sonido acústico real.", R.drawable.ap_550bk, "TECLADOS", 1));
                    pDao.insertar(new ProductoEntity("Teclado Casio CT-S200", 1100.0, "Teclado portátil de 61 teclas con modo de música de baile.", R.drawable.ct_200bk, "TECLADOS", 1));
                    pDao.insertar(new ProductoEntity("Piano Casio AP-300BK", 2330.0, "Sonido estéreo dinámico y diseño compacto moderno de lujo.", R.drawable.ap_300bk, "TECLADOS", 1));

                    // --- 2. POBLAR USUARIOS ---
                    uDao.insertar(new UsuarioEntity("admin@casiostore.com", "123456", "Administrador Principal"));
                    uDao.insertar(new UsuarioEntity("joel@casiostore.com", "password", "Joel Antinapa"));
                }
            });
        }
    };
}