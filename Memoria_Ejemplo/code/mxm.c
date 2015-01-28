#pragma omp parallel for default(shared) private(i, j, k)
for (j = 0; j < n; j++) {
  for (i = 0; i < l; i++) {
    R = 0.0;
    for (k = 0; k < m; k++)
        R += b[i + k * l] * a[k + j * m];
    c[i + j * l] = R;
  }
}
