#include <iostream>
#include <vector>
#include <cmath>
#include <omp.h>

constexpr int CORE_NUMBER = 2;
using namespace std;

void init_array(vector<int>& array) {
    for (size_t i = 0; i < array.size(); i++) {
        array[i] = i + 1;
    }
}

long long part(vector<int>& array, int start, int end) {
    long long result_sum = 0;

    #pragma omp parallel for
    for (int i = start; i <= end; i++) {
        result_sum += array[i];
    }

    return result_sum;
}

int main() {
    cout << "Enter the array size: ";
    int size;

    cin >> size;
    vector<int> array(size);
    init_array(array);

    long long result = 0;
    int step = size > 1 ? size / (log(size) / log(CORE_NUMBER)) : 1;

    #pragma omp parallel for        
    for (int i = 0; i < size; i += step + 1) {
        result += part(array, i, min(i + step, size - 1));
    }

    cout << "Result: " << result << endl;
    return 0;
}
