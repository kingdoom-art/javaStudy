import org.example.collections.MyList;

public class Test {

    private static int passed = 0;
    private static int failed = 0;

    public static void main(String[] args) {
        test_size_empty();
        test_size_afterAdd();
        test_size_afterRemove();

        test_add_toEmpty();
        test_add_multipleKeepsOrder();
        test_add_null();

        test_get_validIndex();
        test_get_onEmpty_throws();
        test_get_negativeIndex_throws();
        test_get_outOfBounds_throws();

        test_remove_first();
        test_remove_last();
        test_remove_middle();
        test_remove_singleElement();
        test_remove_invalidIndex_throws();

        test_fullScenario();
        test_stress_addRemove();

        System.out.printf("%n===== ИТОГ: passed=%d, failed=%d%n", passed, failed);
        if (failed > 0) {
            System.exit(1);
        }
    }

    // ==================== Утилиты ====================

    private static void check(String name, boolean condition) {
        if (condition) {
            passed++;
            System.out.println("[OK]   " + name);
        } else {
            failed++;
            System.out.println("[FAIL] " + name);
        }
    }

    private static void checkEquals(String name, Object expected, Object actual) {
        boolean ok = (expected == null) ? (actual == null) : expected.equals(actual);
        if (ok) {
            passed++;
            System.out.println("[OK]   " + name + " → " + actual);
        } else {
            failed++;
            System.out.println("[FAIL] " + name
                    + " → ожидалось: '" + expected
                    + "', получено: '" + actual + "'");
        }
    }

    private static void checkThrows(String name,
                                    Class<? extends Throwable> expected,
                                    Runnable action) {
        try {
            action.run();
            failed++;
            System.out.println("[FAIL] " + name + " → исключение не брошено");
        } catch (Throwable t) {
            if (expected.isInstance(t)) {
                passed++;
                System.out.println("[OK]   " + name + " → "
                        + t.getClass().getSimpleName());
            } else {
                failed++;
                System.out.println("[FAIL] " + name + " → ожидалось "
                        + expected.getSimpleName() + ", получено "
                        + t.getClass().getSimpleName());
            }
        }
    }

    // ==================== size ====================

    private static void test_size_empty() {
        MyList<Integer> list = new MyList<>();
        checkEquals("size пустого списка", 0, list.size());
        checkEquals("toString пустого списка", "[]", list.toString());
    }

    private static void test_size_afterAdd() {
        MyList<Integer> list = new MyList<>();
        list.add(1);
        checkEquals("size после 1 add", 1, list.size());
        list.add(2);
        list.add(3);
        checkEquals("size после 3 add", 3, list.size());
    }

    private static void test_size_afterRemove() {
        MyList<Integer> list = new MyList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.remove(0);
        checkEquals("size после remove", 2, list.size());
    }

    // ==================== add ====================

    private static void test_add_toEmpty() {
        MyList<Integer> list = new MyList<>();
        list.add(42);
        checkEquals("add в пустой", "[42]", list.toString());
    }

    private static void test_add_multipleKeepsOrder() {
        MyList<Integer> list = new MyList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        checkEquals("add сохраняет порядок", "[1, 2, 3]", list.toString());
    }

    private static void test_add_null() {
        MyList<Integer> list = new MyList<>();
        list.add(null);
        checkEquals("add null", "[null]", list.toString());
        checkEquals("size после add null", 1, list.size());
    }

    // ==================== get ====================

    private static void test_get_validIndex() {
        MyList<Integer> list = new MyList<>();
        list.add(10);
        list.add(20);
        list.add(30);
        checkEquals("get(0)", 10, list.get(0));
        checkEquals("get(1)", 20, list.get(1));
        checkEquals("get(2)", 30, list.get(2));
    }

    private static void test_get_onEmpty_throws() {
        MyList<Integer> list = new MyList<>();
        checkThrows("get(0) на пустом", IndexOutOfBoundsException.class,
                () -> list.get(0));
    }

    private static void test_get_negativeIndex_throws() {
        MyList<Integer> list = new MyList<>();
        list.add(1);
        checkThrows("get(-1)", IndexOutOfBoundsException.class,
                () -> list.get(-1));
    }

    private static void test_get_outOfBounds_throws() {
        MyList<Integer> list = new MyList<>();
        list.add(1);
        list.add(2);
        checkThrows("get(2) при size=2", IndexOutOfBoundsException.class,
                () -> list.get(2));
        checkThrows("get(100)", IndexOutOfBoundsException.class,
                () -> list.get(100));
    }

    // ==================== remove ====================

    private static void test_remove_first() {
        MyList<Integer> list = new MyList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.remove(0);
        checkEquals("remove(0)", "[2, 3]", list.toString());
    }

    private static void test_remove_last() {
        MyList<Integer> list = new MyList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.remove(2);
        checkEquals("remove последнего", "[1, 2]", list.toString());
    }

    private static void test_remove_middle() {
        MyList<Integer> list = new MyList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.remove(1);
        checkEquals("remove из середины", "[1, 3]", list.toString());
    }

    private static void test_remove_singleElement() {
        MyList<Integer> list = new MyList<>();
        list.add(99);
        list.remove(0);
        checkEquals("remove единственного", "[]", list.toString());
        checkEquals("size после remove единственного", 0, list.size());
    }

    private static void test_remove_invalidIndex_throws() {
        MyList<Integer> list = new MyList<>();
        list.add(1);
        checkThrows("remove(-1)", IndexOutOfBoundsException.class,
                () -> list.remove(-1));
        checkThrows("remove(1) при size=1", IndexOutOfBoundsException.class,
                () -> list.remove(1));
    }

    // ==================== Комплексные сценарии ====================

    private static void test_fullScenario() {
        MyList<Integer> list = new MyList<>();

        checkEquals("старт: []", "[]", list.toString());
        checkEquals("старт size=0", 0, list.size());

        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);
        checkEquals("после add x4", "[1, 2, 3, 4]", list.toString());
        checkEquals("size после add x4", 4, list.size());

        checkEquals("get(0)", 1, list.get(0));
        checkEquals("get(3)", 4, list.get(3));

        list.remove(1);
        checkEquals("после remove(1)", "[1, 3, 4]", list.toString());
        checkEquals("size после remove(1)", 3, list.size());

        list.remove(0);
        checkEquals("после remove(0)", "[3, 4]", list.toString());

        list.remove(1);
        checkEquals("после remove(1) (последний)", "[3]", list.toString());

        list.remove(0);
        checkEquals("после remove(0) (единственный)", "[]", list.toString());
        checkEquals("итоговый size", 0, list.size());
    }

    private static void test_stress_addRemove() {
        MyList<Integer> list = new MyList<>();

        for (int i = 0; i < 100; i++) {
            list.add(i);
        }
        checkEquals("stress: size после 100 add", 100, list.size());
        checkEquals("stress: get(0)", 0, list.get(0));
        checkEquals("stress: get(99)", 99, list.get(99));

        for (int i = 0; i < 50; i++) {
            list.remove(0);
        }
        checkEquals("stress: size после 50 remove", 50, list.size());
        checkEquals("stress: get(0) после удалений", 50, list.get(0));

        for (int i = 0; i < 50; i++) {
            list.add(i * 10);
        }
        checkEquals("stress: size после 50 add", 100, list.size());

        String s = list.toString();
        check("stress: toString в формате [..]",
                s.startsWith("[") && s.endsWith("]"));
    }
}