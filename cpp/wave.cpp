#include <iostream>
#include <vector>
#include <cmath>

constexpr int CORE_NUMBER = 2;
int last = 0;
using namespace std;

void init_array(vector<long long>& array) {
    for (int i = 0; i < array.size(); i++) {
        array[i] = i + 1;
    }
}

void put_result(vector<long long>& array, int start, int end) {
    int right;
    for (int i = start; i < end; i++) {
        right = last - i - 1;

        if (i < right) {
            array[i] += array[right];
        }
    }
}

int calculate_step(int size) {
    return size > 1 ? size / (log(size) / log(CORE_NUMBER)) : 1;
}

int main()
{
    cout << "Enter array size: ";
    int size;

    cin >> size;
    vector<long long> array(size);
    init_array(array);

    last = size;
    int middle = last / 2 + last % 2;

    long long sum = 0;
    int step = calculate_step(middle);
    int count = size > 1 ? ceil(log(size) / log(2)) : 1;
    int j = 0;

    for (int i = 0; i < count; i++) {
        while (j <= middle) {
            put_result(array, j, min(j + step, middle));
            j += step;
        }

        last = middle;
        middle = last / 2 + last % 2;
        step = calculate_step(middle);
        j = 0;
    }

    cout << "Result: " << array[0] << endl;
    return 0;
}
