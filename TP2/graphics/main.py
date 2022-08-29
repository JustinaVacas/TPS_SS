import sys
import plotly.graph_objects as go


def draw(dataOrder, iters, eta, N, L, R):
    fig = go.Figure(
        data=go.Scatter(
            x=list(range(1, iters + 1)), y=dataOrder,
            mode='lines',
            marker={'color':'purple'}
        ),
        layout=go.Layout(
            title=dict(text=f'Orden vs Iteraciones [N={N} - L={L} - rc={R} - n={eta}]', x=0.5),
            xaxis=dict(title='Iteraciones'),
            yaxis=dict(title='Orden'),
        )
    )

    # Set figure size
    fig.update_layout(width=1000, height=1000)
    fig.show()


def parseParameters(file):
    with open(file) as ordersFile:
        ordersLines = ordersFile.readlines()

    birds = []
    for line in ordersLines:
        newline = line.strip()
        birds.append(float(newline))

    return birds, len(birds)


if __name__ == '__main__':
    birds, iterations = parseParameters(sys.argv[1])
    draw(birds, iterations, 5, 300, 7, 1)
