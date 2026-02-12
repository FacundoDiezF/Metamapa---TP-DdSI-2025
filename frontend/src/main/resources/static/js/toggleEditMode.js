function toggleEditMode(eventId) {
    var viewCard = document.getElementById('view-' + eventId);
    var editForm = document.getElementById('form-' + eventId);

    if (viewCard && editForm) {
        if (editForm.classList.contains('d-none')) {
            editForm.classList.remove('d-none');
            viewCard.classList.add('d-none');
        } else {
            editForm.classList.add('d-none');
            viewCard.classList.remove('d-none');

            editForm.reset();
        }
    }
}