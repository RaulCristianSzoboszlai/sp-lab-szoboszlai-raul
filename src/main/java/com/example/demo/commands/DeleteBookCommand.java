package com.example.demo.commands;

public class DeleteBookCommand implements Command<Boolean> {

    private final CommandContext context;
    private final Integer id;

    public DeleteBookCommand(CommandContext context, Integer id) {
        this.context = context;
        this.id = id;
    }

    /**
     * @return true if the book existed and was deleted, false otherwise
     */
    @Override
    public Boolean execute() {
        if (!context.getBooksRepository().existsById(id)) {
            return false;
        }
        context.getBooksRepository().deleteById(id);
        return true;
    }
}
