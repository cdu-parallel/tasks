#include <iostream>
#include <vector>
#include <cmath>
#include <omp.h>

constexpr int CORE_NUMBER = 2;
using namespace std;

void init_array(vector<long long>& array) {
    for (int i = 0; i < array.size(); i++) {
        array[i] = i + 1;
    }
}

int main() {
    cout << "Enter array size: ";
    int size;

    cin >> size;
    vector<long long> array(size);
    init_array(array);

    int end = size;
    int middle = end / 2 + end % 2;

    long long sum = 0;
    int count = size > 1 ? ceil(log(size) / log(2)) : 1;

    for (int i = 0; i < count; i++) {
        {
            #pragma omp parallel for
            for (int j = 0; j <= middle; j++) {
                int right = end - j - 1;

                if (j < right) {
                    array[j] += array[right];
                }
            }
        }

        end = middle;
        middle = end / 2 + end % 2;
    }

    cout << "Result: " << array[0] << endl;
    return 0;
}
