double a[N][M], b[N];

for (int i = 0; i < N; i++)
  for (int j = 0; j < M; j++)
    a[i][j] = a[i][j] * b[j];
