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
        UltimaCompraEntity.class,
        NotificacionEntity.class // <-- 1. Añadida la entidad de notificaciones
}, version = 5, exportSchema = false) // <-- 2. Versión actualizada a 4
public abstract class CasioDatabase extends RoomDatabase {

    public abstract ProductoDao productoDao();
    public abstract CarritoDao carritoDao();
    public abstract UsuarioDao usuarioDao();
    public abstract HistorialDao historialDao();
    public abstract UltimaCompraDao ultimaCompraDao();
    public abstract NotificacionDao notificacionDao(); // <-- 3. Declarado el DAO de notificaciones

    private static volatile CasioDatabase INSTANCE;

    public static CasioDatabase getDatabase(final Context context) {
        if (INSTANCE == null) {
            synchronized (CasioDatabase.class) {
                if (INSTANCE == null) {
                    INSTANCE = Room.databaseBuilder(context.getApplicationContext(),
                                    CasioDatabase.class, "casio_store_db")
                            .fallbackToDestructiveMigration() // Borra y recrea la BD automáticamente al cambiar de versión
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

                    // --- NUEVOS PRODUCTOS ADICIONALES ---
// Relojes
                    pDao.insertar(new ProductoEntity("Reloj Casio G-Shock Mudmaster GWG-B1000-3AJF", 4200.0, "Resistencia extrema al barro, polvo y vibraciones con brújula y termómetro.", R.drawable.gwg_b1000, "RELOJES", 1));
                    pDao.insertar(new ProductoEntity("Reloj Casio Pro Trek PRG-600", 3800.0, "Reloj solar para exteriores con altímetro, barómetro y sensor triple.", R.drawable.prg_600, "RELOJES", 1));
                    pDao.insertar(new ProductoEntity("Reloj Casio Vintage A-700WM", 850.0, "Diseño ultra delgado de estilo retro vintage con malla milanesa.", R.drawable.a_700wm, "RELOJES", 1));
                    pDao.insertar(new ProductoEntity("Reloj Casio Edifice Chronograph EFR-574D", 4900.0, "Edición especial de alto rendimiento con conectividad Bluetooth.", R.drawable.efr_574d, "RELOJES", 1));
                    pDao.insertar(new ProductoEntity("Reloj Casio Baby-G BA-110", 2100.0, "Diseño deportivo y femenino con alta resistencia a impactos.", R.drawable.ba_110, "RELOJES", 1));

// Calculadoras
                    pDao.insertar(new ProductoEntity("Calculadora Científica FX-570ES Plus", 290.0, "Calculadora científica no programable con 417 funciones avanzadas.", R.drawable.fx_570esplus, "CALCULADORAS", 1));
                    pDao.insertar(new ProductoEntity("Calculadora Científica ClassWiz FX-991LAX", 450.0, "Pantalla de alta resolución con hoja de cálculo y visualización en código QR.", R.drawable.fx_991lax, "CALCULADORAS", 1));
                    pDao.insertar(new ProductoEntity("Calculadora de Escritorio JW-200TW", 140.0, "Diseño elegante con cuerpo metálico y funciones de cálculo fiscal y de impuestos.", R.drawable.jw_200tw, "CALCULADORAS", 1));
                    pDao.insertar(new ProductoEntity("Calculadora de Impuestos y Costos DH-12", 180.0, "Calculadora de 12 dígitos con teclas grandes y conversiones de moneda.", R.drawable.dh_12, "CALCULADORAS", 1));
                    pDao.insertar(new ProductoEntity("Calculadora de Impuestos JS-40B", 220.0, "Calculadora de sobremesa profesional con funciones de control de tiempo.", R.drawable.js_40b, "CALCULADORAS", 1));

// Teclados y Pianos Digitales
                    pDao.insertar(new ProductoEntity("Teclado Casio CT-X700", 1850.0, "Teclado portátil de 61 teclas con fuente de sonidos AiX y ritmos avanzados.", R.drawable.ct_x700, "TECLADOS", 1));
                    pDao.insertar(new ProductoEntity("Teclado Casio SA-81", 650.0, "Mini teclado de 44 teclas ideal para niños y principiantes con gran variedad de tonos.", R.drawable.sa_81, "TECLADOS", 1));
                    pDao.insertar(new ProductoEntity("Piano Casio Privia PX-S1100", 3800.0, "Piano digital delgado de 88 teclas contrapesadas con Bluetooth MIDI.", R.drawable.px_s1100, "TECLADOS", 1));
                    pDao.insertar(new ProductoEntity("Piano Casio CDP-S110", 2500.0, "Piano digital compacto con acción de martillo escalado y sonido estéreo.", R.drawable.cdp_s110, "TECLADOS", 1));
                    pDao.insertar(new ProductoEntity("Teclado Casio CT-S400", 1450.0, "Teclado portátil inteligente con 600 tonos integrados y rueda de pitch bend.", R.drawable.ct_s400, "TECLADOS", 1));

                    // --- 2. POBLAR USUARIOS ---
                    uDao.insertar(new UsuarioEntity("admin@casiostore.com", "123456", "Administrador Principal"));
                    uDao.insertar(new UsuarioEntity("joel@casiostore.com", "password", "Joel Antinapa"));
                }
            });
        }
    };
}