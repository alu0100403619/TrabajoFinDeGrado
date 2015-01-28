double a[N][M], b[N];

for (int j = 0; j < M; j++)
  for (int i = 0; i < N; i++)
    a[i][j] = a[i][j] * b[j];
